package skillmanagement.domain.projects.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.searchTerms
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectLabel

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO projects (id, data, keywords) VALUES (:id, :data, :keywords)"

    operator fun invoke(project: Project) {
        val parameters = mapOf(
            "id" to project.id.toString(),
            "data" to objectMapper.writeValueAsString(project),
            "keywords" to possibleSearchTerms(project.label)
        )
        jdbcTemplate.update(statement, parameters)
    }

    private fun possibleSearchTerms(label: ProjectLabel): String =
        searchTerms(label.toString()).joinToString(separator = " ")

}
