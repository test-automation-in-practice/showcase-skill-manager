package skillmanagement.domain.employees.usecases.projectassignments.update

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.usecases.get.GetEmployeeById
import skillmanagement.domain.employees.usecases.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class UpdateProjectAssignmentById(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(
        employeeId: UUID,
        projectAssignmentId: UUID,
        block: (ProjectAssignment) -> ProjectAssignment
    ): UpdateProjectAssignmentResult {
        val employee = getEmployeeById(employeeId) ?: return UpdateProjectAssignmentResult.EmployeeNotFound
        val currentAssignment = employee.projects.singleOrNull { it.id == projectAssignmentId }
            ?: return UpdateProjectAssignmentResult.ProjectAssignmentNotFound
        val modifiedAssignment = block(currentAssignment)

        assertNoInvalidModifications(currentAssignment, modifiedAssignment)

        val updatedEmployee = employee.setProjectAssignment(modifiedAssignment)
        updateEmployeeInDataStore(updatedEmployee)
        return UpdateProjectAssignmentResult.SuccessfullyUpdated(modifiedAssignment)
    }

    private fun assertNoInvalidModifications(
        currentAssignment: ProjectAssignment,
        modifiedAssignment: ProjectAssignment
    ) {
        check(currentAssignment.id == modifiedAssignment.id) { "ID must not be changed!" }
        check(currentAssignment.project == modifiedAssignment.project) { "Project must not be changed!" }
    }

}

sealed class UpdateProjectAssignmentResult {
    object EmployeeNotFound : UpdateProjectAssignmentResult()
    object ProjectAssignmentNotFound : UpdateProjectAssignmentResult()
    data class SuccessfullyUpdated(val assignment: ProjectAssignment) : UpdateProjectAssignmentResult()
}
