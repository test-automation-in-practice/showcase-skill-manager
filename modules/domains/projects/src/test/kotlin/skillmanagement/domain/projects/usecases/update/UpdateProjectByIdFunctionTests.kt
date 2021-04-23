package skillmanagement.domain.projects.usecases.update

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.retry.annotation.EnableRetry
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.success
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.model.ProjectUpdatedEvent
import skillmanagement.domain.projects.model.projectId
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotChanged
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotFound
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant

internal class UpdateProjectByIdFunctionTests {

    private val id = projectId()
    private val project = ProjectEntity(
        id = id,
        version = 2,
        data = Project(
            label = ProjectLabel("Old Label"),
            description = ProjectDescription("Old Description")
        ),
        lastUpdate = instant("2021-03-24T12:34:56.789Z")
    )

    @Nested
    @UnitTest
    @ResetMocksAfterEachTest
    inner class FunctionalTests {

        private val getProjectById: GetProjectByIdFunction = mockk()
        private val updateProjectInDataStore: UpdateProjectInDataStoreFunction = mockk()
        private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)

        private val updateProjectById =
            UpdateProjectByIdFunction(getProjectById, updateProjectInDataStore, publishEvent)

        @Test
        fun `updating an existing project stores it in the data store and publishes an event`() {
            val change: (Project) -> (Project) = {
                it.copy(
                    label = ProjectLabel("New Label"),
                    description = ProjectDescription("New Description")
                )
            }

            val expectedChangedProject = project.update(change)
            val expectedUpdatedProject = expectedChangedProject
                .copy(version = 3, lastUpdate = instant("2021-03-24T12:35:06.789Z"))

            every { getProjectById(id) } returns project
            every { updateProjectInDataStore(expectedChangedProject) } answers { simulateUpdate(firstArg()) }

            val result = updateProjectById(id, change)

            result shouldBe success(expectedUpdatedProject)
            verify { publishEvent(ProjectUpdatedEvent(expectedUpdatedProject)) }
        }

        @Test
        fun `updating a non-existing project returns project not found failure`() {
            every { getProjectById(id) } returns null

            val result = updateProjectById(project.id) { it.copy(label = ProjectLabel("New Label")) }

            result shouldBe failure(ProjectNotFound)

            verify { updateProjectInDataStore wasNot called }
            verify { publishEvent wasNot called }
        }

        @Test
        fun `not changing anything during the update returns project not changed failure`() {
            every { getProjectById(id) } returns project

            val result = updateProjectById(project.id) { it }

            result shouldBe failure(ProjectNotChanged(project))

            verify { updateProjectInDataStore wasNot called }
            verify { publishEvent wasNot called }
        }

        private fun simulateUpdate(it: ProjectEntity) =
            it.copy(version = it.version + 1, lastUpdate = it.lastUpdate.plusSeconds(10))

    }

    @Nested
    @ResetMocksAfterEachTest
    @TechnologyIntegrationTest
    @SpringBootTest(classes = [RetryTestsConfiguration::class])
    inner class RetryTests(
        @Autowired private val getProjectById: GetProjectByIdFunction,
        @Autowired private val updateProjectInDataStore: UpdateProjectInDataStoreFunction,
        @Autowired private val updateProjectById: UpdateProjectByIdFunction
    ) {

        private val change: (Project) -> Project = { it.copy(label = ProjectLabel("new")) }

        @Test
        fun `operation is retried up to 5 times in case of concurrent update exceptions`() {
            every { getProjectById(id) } returns project
            every { updateProjectInDataStore(any()) } throws ConcurrentProjectUpdateException()
            assertThrows<ConcurrentProjectUpdateException> {
                updateProjectById(id, change)
            }
            verify(exactly = 5) { getProjectById(id) }
        }

        @Test
        fun `operation does not fail if retrying fixes the problem`() {
            every { getProjectById(id) } returns project
            every { updateProjectInDataStore(any()) }
                .throws(ConcurrentProjectUpdateException())
                .andThenThrows(ConcurrentProjectUpdateException())
                .andThen(project)

            updateProjectById(id, change)

            verify(exactly = 3) { getProjectById(id) }
        }

    }

    @EnableRetry
    @Import(UpdateProjectByIdFunction::class)
    private class RetryTestsConfiguration {

        @Bean
        fun getProjectById(): GetProjectByIdFunction = mockk(relaxed = true)

        @Bean
        fun updateProjectInDataStore(): UpdateProjectInDataStoreFunction = mockk(relaxed = true)

        @Bean
        fun publishEvent(): PublishEventFunction = mockk(relaxed = true)

    }

}
