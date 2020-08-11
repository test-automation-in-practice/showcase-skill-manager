package skillmanagement.domain.employees.usecases.projectassignments.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.find.FindEmployeeIds
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import java.util.UUID

@Component
class DeleteProjectAssignmentsOfEmployeesEventListener(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    @EventListener
    fun handle(event: ProjectDeletedEvent) {
        log.info { "Handling $event" }
        val projectId = event.project.id
        findEmployeeIds(EmployeesWhoWorkedOnProject(projectId))
            .onEach { log.info { "Removing projects assignments of project [$projectId] from employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.removeProjectAssignmentsByProjectId(projectId) }
            }
    }

    private fun Employee.removeProjectAssignmentsByProjectId(projectId: UUID): Employee =
        copy(projects = projects.filter { it.project.id != projectId })

}
