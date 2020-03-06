package skillmanagement.domain.skills.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill

@TechnicalFunction
class InsertSkillIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    @Transactional
    operator fun invoke(skill: Skill) = jdbcTemplate.insert(
        table = "skills",
        columnValues = listOf(
            "id" to "${skill.id}",
            "label" to "${skill.label}"
        )
    )

}
