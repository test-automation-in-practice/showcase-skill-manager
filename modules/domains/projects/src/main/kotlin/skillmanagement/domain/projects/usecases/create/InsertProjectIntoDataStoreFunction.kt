package skillmanagement.domain.projects.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.ProjectEntity

@TechnicalFunction
internal class InsertProjectIntoDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO projects (id, version, data) VALUES (:id, :version, :data)"

    operator fun invoke(project: ProjectEntity) {
        val parameters = mapOf(
            "id" to project.id.toString(),
            "version" to project.version,
            "data" to objectMapper.writeValueAsString(project)
        )
        jdbcTemplate.update(statement, parameters)
    }

}
