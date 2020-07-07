package skillmanagement.domain.skills.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill

@TechnicalFunction
class InsertSkillIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO skills (id, data) VALUES (:id, :data)"

    operator fun invoke(skill: Skill) {
        val parameters = mapOf(
            "id" to skill.id.toString(),
            "data" to objectMapper.writeValueAsString(skill)
        )
        jdbcTemplate.update(statement, parameters)
    }

}
