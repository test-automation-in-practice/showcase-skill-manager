package skillmanagement.domain.skills.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill

@TechnicalFunction
class InsertSkillIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    operator fun invoke(skill: Skill) = jdbcTemplate.insert(
        tableName = "skills",
        columnValueMapping = listOf(
            "id" to "${skill.id}",
            "label" to "${skill.label}"
        )
    )

}
