package skillmanagement.domain.skills.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillRowMapper

@TechnicalFunction
class FindSkillsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val query = "SELECT * FROM skills"

    @Transactional(readOnly = true)
    operator fun invoke(): List<Skill> {
        return jdbcTemplate.query(query, SkillRowMapper)
    }

}
