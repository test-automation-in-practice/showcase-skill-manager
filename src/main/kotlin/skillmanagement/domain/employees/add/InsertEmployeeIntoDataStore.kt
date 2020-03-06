package skillmanagement.domain.employees.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee

@TechnicalFunction
class InsertEmployeeIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    @Transactional
    operator fun invoke(employee: Employee) {
        require(employee.skills.isEmpty()) { "Skills are not supported for inserting new employees!" }
        require(employee.projects.isEmpty()) { "Projects are not supported for inserting new employees!" }

        jdbcTemplate.insert(
            table = "employees",
            columnValues = listOf(
                "id" to employee.id.toString(),
                "first_name" to employee.firstName.toString(),
                "last_name" to employee.lastName.toString(),
                "title" to employee.title.toString(),
                "email" to employee.email.toString(),
                "telephone" to employee.telephone.toString(),
                "last_update_utc" to employee.lastUpdate.toString()
            )
        )
    }

}
