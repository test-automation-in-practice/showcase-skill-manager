package skillmanagement.domain.employees.projectassignments.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.find.FindEmployeeIds
import skillmanagement.domain.employees.update.UpdateEmployeeById
import skillmanagement.domain.projects.model.ProjectDeletedEvent

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

}
