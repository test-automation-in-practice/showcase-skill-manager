package skillmanagement.domain.employees.projectassignments.create

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.projectassignments.create.AssignProjectToEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.projectassignments.create.AssignProjectToEmployeeResult.ProjectNotFound
import skillmanagement.domain.employees.projectassignments.create.AssignProjectToEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.projects.get.GetProjectById
import java.time.LocalDate
import java.util.UUID

@BusinessFunction
class CreateProjectAssignmentForEmployee(
    private val idGenerator: IdGenerator,
    private val getEmployeeById: GetEmployeeById,
    private val getProjectById: GetProjectById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(
        employeeId: UUID,
        projectId: UUID,
        contribution: ProjectContribution,
        startDate: LocalDate,
        endDate: LocalDate?
    ): AssignProjectToEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val project = getProjectById(projectId) ?: return ProjectNotFound

        val assignment = ProjectAssignment(
            id = idGenerator.generateId(),
            project = project,
            contribution = contribution,
            startDate = startDate,
            endDate = endDate
        )

        updateEmployeeInDataStore(employee.addProjectAssignment(assignment))
        return SuccessfullyAssigned(assignment)
    }

}

sealed class AssignProjectToEmployeeResult {
    object EmployeeNotFound : AssignProjectToEmployeeResult()
    object ProjectNotFound : AssignProjectToEmployeeResult()
    data class SuccessfullyAssigned(val assignment: ProjectAssignment) : AssignProjectToEmployeeResult()
}
