package skillmanagement.domain.employees.usecases.projectassignments.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.SuccessfullyDeletedProjectAssignment
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdatedEmployee
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployeeFunction(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, assignmentId: UUID): DeleteProjectAssignmentOfEmployeeResult {
        val updateResult = updateEmployeeById(employeeId) {
            it.removeProjectAssignmentById(assignmentId)
        }
        return when (updateResult) {
            is NotUpdatedBecauseEmployeeNotFound -> EmployeeNotFound
            is NotUpdatedBecauseEmployeeNotChanged -> ProjectAssignmentNotFound
            is SuccessfullyUpdatedEmployee -> SuccessfullyDeletedProjectAssignment(updateResult.employee)
        }
    }

    private fun Employee.removeProjectAssignmentById(assignmentId: UUID): Employee =
        copy(projects = projects.filter { it.id != assignmentId })

}

sealed class DeleteProjectAssignmentOfEmployeeResult {
    object EmployeeNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object ProjectAssignmentNotFound : DeleteProjectAssignmentOfEmployeeResult()
    data class SuccessfullyDeletedProjectAssignment(val employee: Employee) : DeleteProjectAssignmentOfEmployeeResult()
}
