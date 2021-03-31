package skillmanagement.domain.employees.usecases.projectassignments.create

import arrow.core.Either
import org.springframework.util.IdGenerator
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.gateways.GetProjectByIdAdapterFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.model.ProjectContribution
import skillmanagement.domain.employees.usecases.projectassignments.create.CreationFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.create.CreationFailure.ProjectNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.time.LocalDate
import java.util.UUID

@BusinessFunction
class CreateProjectAssignmentForEmployeeFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val getProjectById: GetProjectByIdAdapterFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(
        employeeId: UUID,
        projectId: UUID,
        contribution: ProjectContribution,
        startDate: LocalDate,
        endDate: LocalDate?
    ): Either<CreationFailure, Employee> {
        val project = getProjectById(projectId) ?: return failure(ProjectNotFound)
        val updateResult = updateEmployeeById(employeeId) {
            val assignment = ProjectAssignment(
                id = idGenerator.generateId(),
                project = project,
                contribution = contribution,
                startDate = startDate,
                endDate = endDate
            )
            it.addProjectAssignment(assignment)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> error("should not happen")
            }
        }
    }

    private fun Employee.addProjectAssignment(projectAssignment: ProjectAssignment): Employee =
        copy(projects = projects + projectAssignment)

}

sealed class CreationFailure {
    object EmployeeNotFound : CreationFailure()
    object ProjectNotFound : CreationFailure()
}
