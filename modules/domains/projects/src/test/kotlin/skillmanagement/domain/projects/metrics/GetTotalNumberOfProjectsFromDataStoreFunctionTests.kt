package skillmanagement.domain.projects.metrics

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.create.InsertProjectIntoDataStoreFunction
import skillmanagement.domain.projects.usecases.delete.DeleteProjectFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetTotalNumberOfProjectsFromDataStoreFunctionTests(
    @Autowired jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired objectMapper: ObjectMapper
) {

    private val delete = DeleteProjectFromDataStoreFunction(jdbcTemplate)
    private val insert = InsertProjectIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val getTotalNumberOfProjects = GetTotalNumberOfProjectsFromDataStoreFunction(jdbcTemplate.jdbcTemplate)

    @Test
    fun `returns the total number of project in the data store`() {
        getTotalNumberOfProjects() shouldBe 0

        insert(project_neo)
        getTotalNumberOfProjects() shouldBe 1

        insert(project_morpheus)
        getTotalNumberOfProjects() shouldBe 2

        delete(project_morpheus.id)
        getTotalNumberOfProjects() shouldBe 1
    }

}
