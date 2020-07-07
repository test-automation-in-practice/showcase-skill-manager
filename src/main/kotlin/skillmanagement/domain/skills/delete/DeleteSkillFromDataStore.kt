package skillmanagement.domain.skills.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import java.util.UUID

@TechnicalFunction
class DeleteSkillFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val statement = "DELETE FROM skills WHERE id = :id"

    operator fun invoke(id: UUID) {
        val parameters = mapOf("id" to id.toString())
        jdbcTemplate.update(statement, parameters)
    }

}
