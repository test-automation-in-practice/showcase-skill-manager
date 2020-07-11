package skillmanagement.domain.employees.projectassignments.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.find.FindEmployees
import skillmanagement.domain.projects.ProjectDeletedEvent

@Component
class DeleteProjectAssignmentsOfEmployeesEventListener(
    private val findEmployees: FindEmployees,
    private val deleteProjectAssignmentOfEmployee: DeleteProjectAssignmentOfEmployee
) {

    private val log = logger {}

    @EventListener
    fun handle(event: ProjectDeletedEvent) {
        log.info { "Handling $event" }
        val projectId = event.project.id
        findEmployees(EmployeesWhoWorkedOnProject(projectId))
            .onEach { log.info { "Removing projects assignments of project [$projectId] from employee [${it.id}]" } }
            .forEach { employee ->
                employee.projects
                    .filter { it.project.id == projectId }
                    .forEach { assignment -> deleteProjectAssignmentOfEmployee(employee.id, assignment.id) }
            }
    }

}
