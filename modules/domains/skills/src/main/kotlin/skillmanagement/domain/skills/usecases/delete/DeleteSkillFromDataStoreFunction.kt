package skillmanagement.domain.skills.usecases.delete

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.SkillId

@TechnicalFunction
internal class DeleteSkillFromDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val singleStatement = "DELETE FROM skills WHERE id = :id"
    private val allStatement = "DELETE FROM skills"

    operator fun invoke(id: SkillId) {
        jdbcTemplate.update(singleStatement, mapOf("id" to "$id"))
    }

    operator fun invoke() {
        jdbcTemplate.update(allStatement, EmptySqlParameterSource.INSTANCE)
    }

}
