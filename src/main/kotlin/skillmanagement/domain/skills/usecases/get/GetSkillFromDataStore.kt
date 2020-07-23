package skillmanagement.domain.skills.usecases.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.Skill
import java.util.UUID

@TechnicalFunction
class GetSkillFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val singleIdQuery = "SELECT data FROM skills WHERE id = :id"
    private val multipleIdsQuery = "SELECT data FROM skills WHERE id IN (:ids)"

    private val rowMapper: RowMapper<Skill> = RowMapper { rs, _ ->
        objectMapper.readValue<Skill>(rs.getString("data"))
    }

    operator fun invoke(id: UUID): Skill? {
        val parameters = mapOf("id" to id.toString())
        return jdbcTemplate.query(singleIdQuery, parameters, rowMapper).singleOrNull()
    }

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Skill> {
        if (ids.isEmpty()) return emptyMap()
        val parameters = mapOf("ids" to ids.map { it.toString() })
        return jdbcTemplate.query(multipleIdsQuery, parameters, rowMapper).map { it.id to it }.toMap()
    }

}
