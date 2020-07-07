package skillmanagement.domain.employees.projectassignments.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.find.FindEmployees
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.projects.ProjectDeletedEvent

@Component
class DeleteProjectAssignmentsOfEmployeesEventListener(
    private val findEmployees: FindEmployees,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    private val log = logger {}

    @EventListener
    fun handle(event: ProjectDeletedEvent) {
        log.info { "Handling $event" }
        val projectId = event.project.id
        findEmployees(EmployeesWhoWorkedOnProject(projectId))
            .onEach { log.info { "Removing projects assignments of project [$projectId] from employee [${it.id}]" } }
            .map { it.removeProjectAssignmentsByProjectId(projectId) }
            .forEach { updateEmployeeInDataStore(it) }
    }

}
