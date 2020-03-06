package skillmanagement.domain.employees.projectassignments.create

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.insert
import skillmanagement.common.update
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.ProjectAssignment
import java.time.Clock

@TechnicalFunction
class InsertProjectAssignmentIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val clock: Clock
) {

    @Transactional
    operator fun invoke(employee: Employee, assignment: ProjectAssignment) {
        createNewEntry(employee, assignment)
        updateEmployee(employee)
    }

    private fun createNewEntry(employee: Employee, assignment: ProjectAssignment) {
        jdbcTemplate.insert(
            table = "employee_project_assignments",
            columnValues = listOf(
                "id" to assignment.id.toString(),
                "employee_id" to employee.id.toString(),
                "project_id" to assignment.project.id.toString(),
                "contribution" to assignment.contribution.toString(),
                "start_date" to assignment.startDate.toString(),
                "end_date" to assignment.endDate?.toString()
            )
        )
    }

    private fun updateEmployee(employee: Employee) {
        jdbcTemplate.update(
            table = "employees",
            columnValues = listOf("last_update_utc" to clock.instant().toString()),
            whereConditions = listOf("id" to employee.id.toString())
        )
    }

}
