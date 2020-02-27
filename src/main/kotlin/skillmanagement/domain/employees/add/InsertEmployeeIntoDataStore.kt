package skillmanagement.domain.employees.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee

@TechnicalFunction
class InsertEmployeeIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    operator fun invoke(employee: Employee) {
        require(employee.skills.isEmpty()) { "Skills are not supported for inserting new employees!" }
        require(employee.projects.isEmpty()) { "Projects are not supported for inserting new employees!" }

        jdbcTemplate.insert(
            tableName = "employees",
            columnValueMapping = listOf(
                "id" to employee.id.toString(),
                "first_name" to employee.firstName.toString(),
                "last_name" to employee.lastName.toString()
            )
        )
    }

}
