package skillmanagement.domain.projects.usecases.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import java.util.UUID

@TechnicalFunction
class DeleteProjectFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val statement = "DELETE FROM projects WHERE id = :id"

    operator fun invoke(id: UUID) {
        val parameters = mapOf("id" to id.toString())
        jdbcTemplate.update(statement, parameters)
    }

}
