package skillmanagement.domain.projects.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.read.GetProjectsFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.uuid

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class InsertProjectIntoDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val getProject = GetProjectsFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertProjectIntoDataStore = InsertProjectIntoDataStoreFunction(jdbcTemplate, objectMapper)

    @Test
    fun `inserts complete project data into data store`() {
        val project = project_neo.copy(id = uuid())

        getProject(project.id) shouldBe null
        insertProjectIntoDataStore(project)
        getProject(project.id) shouldBe project
    }

    @Test
    fun `fails when trying to insert project with existing ID`() {
        val id = uuid()

        val project1 = project_neo.copy(id = id)
        val project2 = project_morpheus.copy(id = id)

        insertProjectIntoDataStore(project1)
        assertThrows<DuplicateKeyException> {
            insertProjectIntoDataStore(project2)
        }
    }

}
