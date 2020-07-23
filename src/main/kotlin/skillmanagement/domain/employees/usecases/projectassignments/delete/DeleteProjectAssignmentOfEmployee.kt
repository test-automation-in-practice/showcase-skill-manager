package skillmanagement.domain.employees.usecases.projectassignments.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.usecases.get.GetEmployeeById
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.SuccessfullyDeleted
import skillmanagement.domain.employees.usecases.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(employeeId: UUID, assignmentId: UUID): DeleteProjectAssignmentOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        if (!employee.hasProjectAssignmentById(assignmentId)) {
            return ProjectAssignmentNotFound
        }

        updateEmployeeInDataStore(employee.removeProjectAssignmentById(assignmentId))
        return SuccessfullyDeleted
    }

}

sealed class DeleteProjectAssignmentOfEmployeeResult {
    object EmployeeNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object ProjectAssignmentNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object SuccessfullyDeleted : DeleteProjectAssignmentOfEmployeeResult()
}
