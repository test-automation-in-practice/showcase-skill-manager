package skillmanagement.domain.projects.get

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import java.sql.ResultSet
import java.util.UUID

@TechnicalFunction
class GetProjectFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val singleQuery = "SELECT * FROM projects WHERE id = :id"
    private val multipleQuery = "SELECT * FROM projects WHERE id IN (:id)"

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

private object ProjectRowMapper : RowMapper<Project> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Project(id = rs.id, label = rs.label, description = rs.description)

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.label: ProjectLabel
        get() = getString("label").let(::ProjectLabel)
    private val ResultSet.description: ProjectDescription
        get() = getString("description").let(::ProjectDescription)
}
