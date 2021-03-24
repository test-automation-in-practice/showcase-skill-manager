package skillmanagement.domain.projects.usecases.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_orbis
import skillmanagement.domain.projects.usecases.create.InsertProjectIntoDataStoreFunction
import skillmanagement.domain.projects.usecases.read.GetProjectsFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.fixedClock

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class UpdateProjectInDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val clock = fixedClock("2020-07-16T12:34:56.789Z")

    private val getProject = GetProjectsFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertProjectIntoDataStore = InsertProjectIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val updateProjectInDataStore = UpdateProjectInDataStoreFunction(jdbcTemplate, objectMapper, clock)

    @Test
    fun `updating an existing project actually persists it`() {
        insertProjectIntoDataStore(project_neo)
        val updatedProject = updateProjectInDataStore(project_neo)
        val loadedProject = getProject(project_neo.id)
        assertThat(updatedProject).isEqualTo(loadedProject)
    }

    @Test
    fun `updating an existing project increments it's version and set the last update timestamp`() {
        insertProjectIntoDataStore(project_neo)
        val updatedProject = updateProjectInDataStore(project_neo)
        assertThat(updatedProject).isNotEqualTo(project_neo)
        assertThat(updatedProject.version).isEqualTo(2)
        assertThat(updatedProject.lastUpdate).isEqualTo(clock.instant())
    }

    /**
     * An existence check before executing the update would prevent this,
     * but also add another round trip to the database...
     **/
    @Test
    fun `updating a non-existing project fails with slightly wrong exception`() {
        assertThrows<ConcurrentProjectUpdateException> {
            updateProjectInDataStore(project_morpheus)
        }
    }

    /**
     * An existence check before executing the update would prevent this,
     * but also add another round trip to the database...
     **/
    @Test
    fun `updating an existing project based on data from an old version throws exception`() {
        insertProjectIntoDataStore(project_orbis.copy(version = 42))
        assertThrows<ConcurrentProjectUpdateException> {
            updateProjectInDataStore(project_orbis.copy(version = 41))
        }
    }

}
