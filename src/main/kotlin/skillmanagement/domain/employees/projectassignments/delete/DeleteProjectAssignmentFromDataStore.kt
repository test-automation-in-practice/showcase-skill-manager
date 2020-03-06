package skillmanagement.domain.employees.projectassignments.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.delete
import skillmanagement.common.update
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.ProjectAssignment
import java.time.Clock

@TechnicalFunction
class DeleteProjectAssignmentFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val clock: Clock
) {

    @Transactional
    operator fun invoke(employee: Employee, assignment: ProjectAssignment) {
        deleteExistingEntry(assignment)
        updateEmployee(employee)
    }

    private fun deleteExistingEntry(assignment: ProjectAssignment) {
        jdbcTemplate.delete(
            table = "employee_project_assignments",
            whereConditions = listOf(
                "id" to assignment.id.toString()
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
