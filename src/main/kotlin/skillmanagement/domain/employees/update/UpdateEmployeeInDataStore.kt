package skillmanagement.domain.employees.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import java.time.Clock

@TechnicalFunction
class UpdateEmployeeInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = "UPDATE employees SET data = :data WHERE id = :id"

    operator fun invoke(employee: Employee) {
        doUpdateEmployee(employee.copy(lastUpdate = clock.instant()))
    }

    private fun doUpdateEmployee(employee: Employee) {
        val parameters = mapOf(
            "id" to employee.id.toString(),
            "data" to objectMapper.writeValueAsString(employee)
        )
        jdbcTemplate.update(statement, parameters)
    }

}
