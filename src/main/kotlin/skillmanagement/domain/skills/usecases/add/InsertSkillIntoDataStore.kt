package skillmanagement.domain.skills.usecases.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.searchTerms
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillLabel


@TechnicalFunction
class InsertSkillIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = "INSERT INTO skills (id, version, data, keywords) VALUES (:id, :version, :data, :keywords)"

    operator fun invoke(skill: Skill) {
        val parameters = mapOf(
            "id" to skill.id.toString(),
            "version" to skill.version,
            "data" to objectMapper.writeValueAsString(skill),
            "keywords" to possibleSearchTerms(skill.label)
        )
        jdbcTemplate.update(statement, parameters)
    }

    private fun possibleSearchTerms(label: SkillLabel): String =
        searchTerms(label.toString()).joinToString(separator = " ")

}
