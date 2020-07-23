package skillmanagement.domain.projects.usecases.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.search.searchTerms
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectLabel

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement =
        "INSERT INTO projects (id, version, data, keywords) VALUES (:id, :version, :data, :keywords)"

    operator fun invoke(project: Project) {
        val parameters = mapOf(
            "id" to project.id.toString(),
            "version" to project.version,
            "data" to objectMapper.writeValueAsString(project),
            "keywords" to possibleSearchTerms(project.label)
        )
        jdbcTemplate.update(statement, parameters)
    }

    private fun possibleSearchTerms(label: ProjectLabel): String =
        searchTerms(label.toString()).joinToString(separator = " ")

}
