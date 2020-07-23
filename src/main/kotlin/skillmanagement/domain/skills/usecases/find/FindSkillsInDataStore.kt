package skillmanagement.domain.skills.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.searchTerms
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.model.Skill

@TechnicalFunction
class FindSkillsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allSkillsQuery = "SELECT data FROM skills"
    private val skillsWithLabelLikeQuery = "SELECT data FROM skills WHERE keywords LIKE :keywords"

    private val rowMapper: RowMapper<Skill> = RowMapper { rs, _ ->
        objectMapper.readValue<Skill>(rs.getString("data"))
    }

    operator fun invoke(query: FindSkillsQuery): List<Skill> = when (query) {
        is NoOpQuery -> queryAllSkills()
        is SkillsWithLabelLike -> querySkillsWithLabelLike(query)
    }

    private fun queryAllSkills(): List<Skill> =
        jdbcTemplate.query(allSkillsQuery, emptyMap<String, Any>(), rowMapper)

    private fun querySkillsWithLabelLike(query: SkillsWithLabelLike): List<Skill> {
        val searchTerms = searchTerms(query.searchTerms)
            .takeIf { it.isNotEmpty() }
            ?: return emptyList()

        val keywords = searchTerms.joinToString(prefix = "%", separator = "%", postfix = "%")
        return jdbcTemplate.query(skillsWithLabelLikeQuery, mapOf("keywords" to keywords), rowMapper)
    }

}
