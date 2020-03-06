package skillmanagement.domain.employees.projectassignments.create

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.projects.get.GetProjectById
import java.time.LocalDate
import java.util.UUID

@BusinessFunction
class CreateProjectAssignmentForEmployee(
    private val idGenerator: IdGenerator,
    private val getEmployeeById: GetEmployeeById,
    private val getProjectById: GetProjectById,
    private val insertProjectAssignmentIntoDataStore: InsertProjectAssignmentIntoDataStore
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(
        employeeId: UUID,
        projectId: UUID,
        contribution: ProjectContribution,
        startDate: LocalDate,
        endDate: LocalDate?
    ): AssignProjectToEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return AssignProjectToEmployeeResult.EmployeeNotFound
        val project = getProjectById(projectId) ?: return AssignProjectToEmployeeResult.ProjectNotFound

        val assignment = ProjectAssignment(
            id = idGenerator.generateId(),
            project = project,
            contribution = contribution,
            startDate = startDate,
            endDate = endDate
        )
        insertProjectAssignmentIntoDataStore(employee, assignment)

        return AssignProjectToEmployeeResult.SuccessfullyAssigned(assignment)
    }

}

sealed class AssignProjectToEmployeeResult {
    object EmployeeNotFound : AssignProjectToEmployeeResult()
    object ProjectNotFound : AssignProjectToEmployeeResult()
    data class SuccessfullyAssigned(val assignment: ProjectAssignment) : AssignProjectToEmployeeResult()
}
