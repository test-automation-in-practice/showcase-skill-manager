package skillmanagement.domain.skills

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

object SkillRowMapper : RowMapper<Skill> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Skill(id = rs.id, label = rs.label)

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.label: SkillLabel
        get() = getString("label").let(::SkillLabel)
}
