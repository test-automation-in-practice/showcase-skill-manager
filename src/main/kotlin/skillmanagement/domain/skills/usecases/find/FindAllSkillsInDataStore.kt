package skillmanagement.domain.skills.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.JdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.Skill

@TechnicalFunction
class FindAllSkillsInDataStore(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allSkillsQuery = "SELECT data FROM skills"

    operator fun invoke(): List<Skill> =
        jdbcTemplate.query(allSkillsQuery) { rs, _ ->
            objectMapper.readValue<Skill>(rs.getString("data"))
        }

}
