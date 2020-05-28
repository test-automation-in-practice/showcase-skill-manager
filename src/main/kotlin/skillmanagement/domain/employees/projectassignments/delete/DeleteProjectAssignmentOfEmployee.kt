package skillmanagement.domain.employees.projectassignments.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.SuccessfullyDeleted
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val deleteProjectAssignmentFromDataStore: DeleteProjectAssignmentFromDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(employeeId: UUID, assignmentId: UUID): DeleteProjectAssignmentOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val assignment = employee.projects.singleOrNull { it.id == assignmentId } ?: return ProjectAssignmentNotFound

        deleteProjectAssignmentFromDataStore(employee, assignment)
        return SuccessfullyDeleted
    }

}

sealed class DeleteProjectAssignmentOfEmployeeResult {
    object EmployeeNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object ProjectAssignmentNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object SuccessfullyDeleted : DeleteProjectAssignmentOfEmployeeResult()
}
