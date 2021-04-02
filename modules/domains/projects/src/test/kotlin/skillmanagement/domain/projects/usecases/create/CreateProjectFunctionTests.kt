package skillmanagement.domain.projects.usecases.create

import io.kotlintest.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.model.projectId
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.fixedClock
import skillmanagement.test.uuid
import java.time.Instant

@UnitTest
@ResetMocksAfterEachTest
internal class CreateProjectFunctionTests {

    private val idGenerator: IdGenerator = mockk()
    private val insertProjectIntoDataStore: InsertProjectIntoDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val clock = fixedClock("2021-03-24T12:34:56.789Z")

    private val createProject = CreateProjectFunction(idGenerator, insertProjectIntoDataStore, publishEvent, clock)

    @Test
    fun `correct Skill instance is constructed and stored for min data`() {
        every { idGenerator.generateId() } returns uuid("9f3d85ac-1571-4404-af3a-6fda482a6c23")

        val actual = createProject(
            ProjectCreationData(
                label = ProjectLabel("Project #1"),
                description = ProjectDescription("Description #1")
            )
        )
        val expected = Project(
            id = projectId("9f3d85ac-1571-4404-af3a-6fda482a6c23"),
            version = 1,
            label = ProjectLabel("Project #1"),
            description = ProjectDescription("Description #1"),
            lastUpdate = Instant.parse("2021-03-24T12:34:56.789Z")
        )
        actual shouldBe expected

        verify {
            insertProjectIntoDataStore(expected)
            publishEvent(ProjectAddedEvent(expected))
        }
        confirmVerified(insertProjectIntoDataStore, publishEvent)
    }

}
