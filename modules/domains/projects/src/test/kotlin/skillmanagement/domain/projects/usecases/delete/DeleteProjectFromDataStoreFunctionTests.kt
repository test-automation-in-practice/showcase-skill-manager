package skillmanagement.domain.projects.usecases.delete

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.projects.model.projectId
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.create.InsertProjectIntoDataStoreFunction
import skillmanagement.domain.projects.usecases.read.GetProjectsFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class DeleteProjectFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val getProject = GetProjectsFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertProjectIntoDataStore = InsertProjectIntoDataStoreFunction(jdbcTemplate, objectMapper)

    private val deleteProjectFromDataStore = DeleteProjectFromDataStoreFunction(jdbcTemplate)

    @Test
    fun `does not fail if project with id does not exist`() {
        val id = projectId()
        getProject(id) shouldBe null
        deleteProjectFromDataStore(id)
    }

    @Test
    fun `deletes existing project from data store`() {
        val project = project_neo
        val id = project.id

        getProject(id) shouldBe null
        insertProjectIntoDataStore(project)
        getProject(id) shouldBe project
        deleteProjectFromDataStore(id)
        getProject(id) shouldBe null
    }

}
