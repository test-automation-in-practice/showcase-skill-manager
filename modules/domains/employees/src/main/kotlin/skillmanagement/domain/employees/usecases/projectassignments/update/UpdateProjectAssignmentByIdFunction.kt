package skillmanagement.domain.employees.usecases.projectassignments.update

import arrow.core.Either
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.model.ProjectAssignmentId
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.ProjectAssignmentNotChanged
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction

@BusinessFunction
class UpdateProjectAssignmentByIdFunction internal constructor(
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction
) {

    // TODO: extract update function to Employee

    operator fun invoke(
        employeeId: EmployeeId,
        projectAssignmentId: ProjectAssignmentId,
        block: (ProjectAssignment) -> ProjectAssignment
    ): Either<UpdateFailure, EmployeeEntity> {
        var assignmentExists = false
        val updateResult = updateEmployeeEntityById(employeeId) { employee ->
            val updatedProjects = employee.projects
                .map { projectAssignment ->
                    if (projectAssignment.id == projectAssignmentId) {
                        assignmentExists = true
                        update(projectAssignment, block)
                    } else {
                        projectAssignment
                    }
                }
            employee.copy(projects = updatedProjects)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> when (assignmentExists) {
                    true -> ProjectAssignmentNotChanged(failure.employee)
                    false -> ProjectAssignmentNotFound
                }
            }
        }
    }

    private fun update(
        currentAssignment: ProjectAssignment,
        block: (ProjectAssignment) -> ProjectAssignment
    ): ProjectAssignment {
        val modifiedAssignment = block(currentAssignment)
        check(currentAssignment.id == modifiedAssignment.id) { "ID must not be changed!" }
        check(currentAssignment.project == modifiedAssignment.project) { "Project must not be changed!" }
        return modifiedAssignment
    }

}

sealed class UpdateFailure {
    object EmployeeNotFound : UpdateFailure()
    object ProjectAssignmentNotFound : UpdateFailure()
    data class ProjectAssignmentNotChanged(val employee: EmployeeEntity) : UpdateFailure()
}
