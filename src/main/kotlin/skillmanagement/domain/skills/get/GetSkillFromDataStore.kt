package skillmanagement.domain.skills.get

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import java.sql.ResultSet
import java.util.UUID

@TechnicalFunction
class GetSkillFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val singleQuery = "SELECT * FROM skills WHERE id = :id"
    private val multipleQuery = "SELECT * FROM skills WHERE id IN (:id)"

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

private object SkillRowMapper : RowMapper<Skill> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Skill(id = rs.id, label = rs.label)

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.label: SkillLabel
        get() = getString("label").let(::SkillLabel)
}
