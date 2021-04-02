package skillmanagement.domain.employees.usecases.projectassignments.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteProjectAssignmentOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, assignmentId: UUID) =
        updateEmployeeById(employeeId) { employee ->
            employee.removeProjectAssignment { it.id == assignmentId }
        }

}
