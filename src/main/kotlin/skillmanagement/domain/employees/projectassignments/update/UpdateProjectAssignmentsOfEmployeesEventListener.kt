package skillmanagement.domain.employees.projectassignments.update

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.find.FindEmployeeIds
import skillmanagement.domain.employees.update.UpdateEmployeeById
import skillmanagement.domain.projects.ProjectUpdatedEvent

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
