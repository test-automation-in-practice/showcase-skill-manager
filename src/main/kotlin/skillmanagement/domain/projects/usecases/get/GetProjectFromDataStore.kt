package skillmanagement.domain.projects.usecases.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.model.Project
import java.util.UUID

@TechnicalFunction
class GetProjectFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val singleIdQuery = "SELECT data FROM projects WHERE id = :id"
    private val multipleIdsQuery = "SELECT data FROM projects WHERE id IN (:ids)"

    private val rowMapper: RowMapper<Project> = RowMapper { rs, _ ->
        objectMapper.readValue<Project>(rs.getString("data"))
    }

    operator fun invoke(id: UUID): Project? {
        val parameters = mapOf("id" to id.toString())
        return jdbcTemplate.query(singleIdQuery, parameters, rowMapper).singleOrNull()
    }

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Project> {
        if (ids.isEmpty()) return emptyMap()
        val parameters = mapOf("ids" to ids.map { it.toString() })
        return jdbcTemplate.query(multipleIdsQuery, parameters, rowMapper).map { it.id to it }.toMap()
    }

}
