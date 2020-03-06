package skillmanagement.domain.projects.get

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectRowMapper
import java.util.UUID

@TechnicalFunction
class GetProjectFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val singleQuery = "SELECT * FROM projects WHERE id = :id"
    private val multipleQuery = "SELECT * FROM projects WHERE id IN (:id)"

    @Transactional(readOnly = true)
    operator fun invoke(id: UUID): Project? {
        return query(singleQuery, mapOf("id" to "$id"))
            .firstOrNull()
    }

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Project> {
        if (ids.isEmpty()) return emptyMap()
        return query(multipleQuery, mapOf("id" to ids.map { "$it" }))
            .map { it.id to it }
            .toMap()
    }

    private fun query(query: String, parameters: Map<String, Any>) =
        jdbcTemplate.query(query, parameters, ProjectRowMapper)

}
