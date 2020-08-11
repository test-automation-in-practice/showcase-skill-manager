package skillmanagement.domain.projects.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.JdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.Project

@TechnicalFunction
class FindAllProjectsInDataStore(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allProjectsQuery = "SELECT data FROM projects"

    operator fun invoke(): List<Project> =
        jdbcTemplate.query(allProjectsQuery) { rs, _ ->
            objectMapper.readValue<Project>(rs.getString("data"))
        }

}
