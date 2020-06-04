package skillmanagement.domain.employees.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeRowMapper

@TechnicalFunction
class FindEmployeesInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeesQuery = "SELECT * FROM employees"

    @Transactional(readOnly = true)
    operator fun invoke(): List<Employee> {
        return jdbcTemplate.query(employeesQuery, EmployeeRowMapper)
    }

}
