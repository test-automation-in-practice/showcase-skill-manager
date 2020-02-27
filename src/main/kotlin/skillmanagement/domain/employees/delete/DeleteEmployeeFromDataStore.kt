package skillmanagement.domain.employees.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import java.util.*

@TechnicalFunction
class DeleteEmployeeFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val statement = "DELETE FROM employees WHERE id = :id"

    operator fun invoke(id: UUID) {
        jdbcTemplate.update(statement, mapOf("id" to "$id"))
    }

}
