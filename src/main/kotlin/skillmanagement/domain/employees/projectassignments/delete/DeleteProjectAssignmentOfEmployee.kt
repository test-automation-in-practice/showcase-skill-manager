package skillmanagement.domain.employees.projectassignments.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.projectassignments.delete.DeleteProjectAssignmentOfEmployeeResult.SuccessfullyDeleted
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(employeeId: UUID, assignmentId: UUID): DeleteProjectAssignmentOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        if (!employee.hasProjectAssignment(assignmentId)) {
            return ProjectAssignmentNotFound
        }

        updateEmployeeInDataStore(employee.removeProjectAssignmentById(assignmentId))
        return SuccessfullyDeleted
    }

    private fun Employee.hasProjectAssignment(assignmentId: UUID): Boolean =
        projects.any { it.id == assignmentId }

    private fun Employee.removeProjectAssignmentById(assignmentId: UUID): Employee =
        copy(projects = projects.filter { it.id != assignmentId })

}

sealed class DeleteProjectAssignmentOfEmployeeResult {
    object EmployeeNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object ProjectAssignmentNotFound : DeleteProjectAssignmentOfEmployeeResult()
    object SuccessfullyDeleted : DeleteProjectAssignmentOfEmployeeResult()
}
