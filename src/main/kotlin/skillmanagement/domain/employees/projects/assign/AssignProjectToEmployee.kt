package skillmanagement.domain.employees.projects.assign

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.projects.get.GetProjectById
import java.time.LocalDate
import java.util.*

@BusinessFunction
class AssignProjectToEmployee(
    private val idGenerator: IdGenerator,
    private val getEmployeeById: GetEmployeeById,
    private val getProjectById: GetProjectById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
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

        val updatedProjects = employee.projects + assignment

        // TODO: use direct update statement instead of updating the whole document
        // https://stackoverflow.com/questions/38261838/add-object-to-an-array-in-java-mongodb
        updateEmployeeInDataStore(employee.copy(projects = updatedProjects))

        return AssignProjectToEmployeeResult.SuccessfullyAssigned(assignment)
    }

}

sealed class AssignProjectToEmployeeResult {
    object EmployeeNotFound : AssignProjectToEmployeeResult()
    object ProjectNotFound : AssignProjectToEmployeeResult()
    data class SuccessfullyAssigned(val assignment: ProjectAssignment) : AssignProjectToEmployeeResult()
}
