package skillmanagement.domain.employees.usecases.projectassignments.update

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.usecases.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.find.FindEmployeeIds
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import skillmanagement.domain.projects.model.ProjectUpdatedEvent

@Component
class UpdateProjectAssignmentsOfEmployeesEventListener(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    @EventListener
    fun handle(event: ProjectUpdatedEvent) {
        log.info { "Handling $event" }
        val projectId = event.project.id
        findEmployeeIds(EmployeesWhoWorkedOnProject(projectId))
            .onEach { log.info { "Updating project assignments for project [$projectId] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.updateProjectAssignmentsOfProject(event.project) }
            }
    }

}
