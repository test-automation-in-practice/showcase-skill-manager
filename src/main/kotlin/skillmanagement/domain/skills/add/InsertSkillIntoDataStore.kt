package skillmanagement.domain.skills.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.searchTerms
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel


@TechnicalFunction
class InsertSkillIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO skills (id, data, keywords) VALUES (:id, :data, :keywords)"

    operator fun invoke(skill: Skill) {
        val parameters = mapOf(
            "id" to skill.id.toString(),
            "data" to objectMapper.writeValueAsString(skill),
            "keywords" to possibleSearchTerms(skill.label)
        )
        jdbcTemplate.update(statement, parameters)
    }

    private fun possibleSearchTerms(label: SkillLabel): String =
        searchTerms(label.toString()).joinToString(separator = " ")

}
