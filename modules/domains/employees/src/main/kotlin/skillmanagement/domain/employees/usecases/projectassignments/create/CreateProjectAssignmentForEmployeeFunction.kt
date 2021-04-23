package skillmanagement.domain.employees.usecases.projectassignments.create

import org.springframework.util.IdGenerator
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.model.ProjectAssignmentId
import skillmanagement.domain.employees.model.ProjectContribution
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction
import java.time.LocalDate

@BusinessFunction
class CreateProjectAssignmentForEmployeeFunction internal constructor(
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction,
    private val idGenerator: IdGenerator
) {

    operator fun invoke(employeeId: EmployeeId, data: ProjectAssignmentCreationData) =
        updateEmployeeEntityById(employeeId) { employee ->
            employee.addOrUpdateProjectAssignment(projectAssignment(data))
        }

    private fun projectAssignment(data: ProjectAssignmentCreationData) =
        ProjectAssignment(
            id = ProjectAssignmentId(idGenerator.generateId()),
            project = data.project,
            contribution = data.contribution,
            startDate = data.startDate,
            endDate = data.endDate
        )

}

data class ProjectAssignmentCreationData(
    val project: ProjectData,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)
