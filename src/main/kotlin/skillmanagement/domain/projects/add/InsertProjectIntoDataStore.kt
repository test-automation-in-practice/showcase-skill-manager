package skillmanagement.domain.projects.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO projects (id, data) VALUES (:id, :data)"

    operator fun invoke(project: Project) {
        val parameters = mapOf(
            "id" to project.id.toString(),
            "data" to objectMapper.writeValueAsString(project)
        )
        jdbcTemplate.update(statement, parameters)
    }

}
