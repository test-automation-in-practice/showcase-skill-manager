package skillmanagement.domain.employees.projects.assign

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.ProjectAssignment

@TechnicalFunction
class InsertProjectAssignmentIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    operator fun invoke(employee: Employee, assignment: ProjectAssignment) {
        jdbcTemplate.insert(
            tableName = "employee_project_assignments",
            columnValueMapping = listOf(
                "id" to assignment.id.toString(),
                "employee_id" to employee.id.toString(),
                "project_id" to assignment.project.id.toString(),
                "contribution" to assignment.contribution.toString(),
                "start_date" to assignment.startDate.toString(),
                "end_date" to assignment.endDate?.toString()
            )
        )
    }

}
