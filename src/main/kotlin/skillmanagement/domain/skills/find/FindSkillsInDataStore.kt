package skillmanagement.domain.skills.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill

@TechnicalFunction
class FindSkillsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val query = "SELECT data FROM skills"

    private val rowMapper: RowMapper<Skill> = RowMapper { rs, _ ->
        objectMapper.readValue<Skill>(rs.getString("data"))
    }

    operator fun invoke(): List<Skill> {
        return jdbcTemplate.query(query, emptyMap<String, Any>(), rowMapper)
    }

}
