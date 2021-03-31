package skillmanagement.domain.employees.usecases.projectassignments.delete

import arrow.core.Either
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeletionFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeletionFailure.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, assignmentId: UUID): Either<DeletionFailure, Employee> {
        val updateResult = updateEmployeeById(employeeId) {
            it.removeProjectAssignmentById(assignmentId)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> ProjectAssignmentNotFound
            }
        }
    }

    private fun Employee.removeProjectAssignmentById(assignmentId: UUID): Employee =
        copy(projects = projects.filter { it.id != assignmentId })

}

sealed class DeletionFailure {
    object EmployeeNotFound : DeletionFailure()
    object ProjectAssignmentNotFound : DeletionFailure()
}
