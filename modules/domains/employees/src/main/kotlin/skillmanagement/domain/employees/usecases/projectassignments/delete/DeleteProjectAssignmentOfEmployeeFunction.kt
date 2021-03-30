package skillmanagement.domain.employees.usecases.projectassignments.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.SuccessfullyDeletedProjectAssignment
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, assignmentId: UUID): DeleteProjectAssignmentOfEmployeeResult {
        val updateResult = updateEmployeeById(employeeId) {
            it.removeProjectAssignmentById(assignmentId)
        }
        return when (updateResult) {
            is UpdateEmployeeByIdResult.EmployeeNotFound -> EmployeeNotFound
            is EmployeeNotChanged -> ProjectAssignmentNotFound
            is SuccessfullyUpdated -> SuccessfullyDeletedProjectAssignment(updateResult.employee)
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
