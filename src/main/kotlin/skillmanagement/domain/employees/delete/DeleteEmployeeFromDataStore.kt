package skillmanagement.domain.employees.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.deleteById
import skillmanagement.domain.TechnicalFunction
import java.util.UUID

@TechnicalFunction
class DeleteEmployeeFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    @Transactional
    operator fun invoke(id: UUID) {
        jdbcTemplate.deleteById(table = "employees", id = id)
    }

}
