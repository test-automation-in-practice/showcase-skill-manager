package skillmanagement.domain.skills.get

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillRowMapper
import java.util.UUID

@TechnicalFunction
class GetSkillFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val singleQuery = "SELECT * FROM skills WHERE id = :id"
    private val multipleQuery = "SELECT * FROM skills WHERE id IN (:id)"

    @Transactional(readOnly = true)
    operator fun invoke(id: UUID): Skill? {
        return query(singleQuery, mapOf("id" to "$id"))
            .firstOrNull()
    }

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Skill> {
        if (ids.isEmpty()) return emptyMap()
        return query(multipleQuery, mapOf("id" to ids.map { "$it" }))
            .map { it.id to it }
            .toMap()
    }

    private fun query(query: String, parameters: Map<String, Any>) =
        jdbcTemplate.query(query, parameters, SkillRowMapper)

}
