package skillmanagement.domain.employees.usecases.projectassignments.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.ProjectAssignmentId
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction

@BusinessFunction
class DeleteProjectAssignmentOfEmployeeFunction internal constructor(
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction
) {

    operator fun invoke(employeeId: EmployeeId, assignmentId: ProjectAssignmentId) =
        updateEmployeeEntityById(employeeId) { employee ->
            employee.removeProjectAssignment { it.id == assignmentId }
        }

}
