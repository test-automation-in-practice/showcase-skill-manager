package skillmanagement.domain.projects.usecases.read

import com.fasterxml.jackson.databind.ObjectMapper
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import info.novatec.testit.logrecorder.logback.junit5.RecordLoggers
import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.projectId
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_orbis
import skillmanagement.domain.projects.usecases.create.InsertProjectIntoDataStoreFunction
import skillmanagement.domain.projects.usecases.delete.DeleteProjectFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetProjectsFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val insertProjectIntoDataStore = InsertProjectIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val deleteProjectFromDataStore = DeleteProjectFromDataStoreFunction(jdbcTemplate)
    private val getProjectsFromDataStore = GetProjectsFromDataStoreFunction(jdbcTemplate, objectMapper)

    @AfterEach
    fun deleteProjects() {
        deleteProjectFromDataStore()
    }

    @Nested
    inner class SingleId {

        @Test
        fun `returns NULL if nothing found with given ID`() {
            getSingleProject(projectId()) shouldBe null
        }

        @Test
        fun `returns Project if found by its ID`() {
            insert(project_neo)
            getSingleProject(project_neo.id) shouldBe project_neo
        }

        private fun getSingleProject(id: ProjectId) = getProjectsFromDataStore(id)

    }

    @Nested
    inner class MultipleIds {

        @Test
        fun `returns empty Map for empty ID list`() {
            getMultipleProjects() shouldBe emptyMap()
        }

        @Test
        fun `returns empty map if none of the Projects were found`() {
            getMultipleProjects(projectId()) shouldBe emptyMap()
        }

        @Test
        fun `returns map with every found Project`() {
            insert(project_neo, project_orbis)

            val actualProjects = getMultipleProjects(project_neo.id, projectId(), project_orbis.id)
            val expectedProjects = setOf(project_neo, project_orbis).map { it.id to it }.toMap()

            actualProjects shouldBe expectedProjects
        }

        private fun getMultipleProjects(vararg ids: ProjectId) = getProjectsFromDataStore(ids.toList(), chunkSize = 2)

    }

    @Nested
    @ResetMocksAfterEachTest
    inner class AllWithCallback {

        private val callback: (ProjectEntity) -> Unit = mockk(relaxed = true)

        @Test
        fun `callback is never invoked if there are no projects`() {
            execute()
            verify { callback wasNot called }
        }

        @Test
        fun `callback is invoked for each existing project`() {
            insert(project_neo, project_orbis)

            execute()

            verify {
                callback(project_neo)
                callback(project_orbis)
            }
            confirmVerified(callback)
        }

        private fun execute() = getProjectsFromDataStore(callback)

    }

    @Nested
    inner class Deserialization {

        private val project = project_neo
        private val projectId = project.id

        @Test
        @RecordLoggers(ProjectRowMapper::class)
        fun `deserialization errors are logged but don't throw an exception`(log: LogRecord) {
            insert(project)

            assertThat(getProjectsFromDataStore(projectId)).isNotNull()
            corruptData(projectId)
            assertThat(getProjectsFromDataStore(projectId)).isNull()

            assertThat(log) {
                containsInOrder {
                    error(startsWith("Could not read data of project [$projectId]: Instantiation of"))
                    debug(startsWith("Corrupted data: {}"))
                }
            }
        }

        private fun corruptData(projectId: ProjectId) {
            jdbcTemplate.update("UPDATE projects SET data = '{}' WHERE id = :id", mapOf("id" to "$projectId"))
        }

    }

    private fun insert(vararg projects: ProjectEntity) = projects.forEach { insertProjectIntoDataStore(it) }

}
