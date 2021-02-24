package skillmanagement.domain.employees.usecases.projectassignments.update

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.messaging.QUEUE_PREFIX
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.messaging.eventBinding
import skillmanagement.common.search.PageSize
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.find.FindEmployeeIds
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectUpdatedEvent

private const val CONTEXT = "ProjectAssignmentUpdatingEventHandler"
private const val PROJECT_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectUpdatedEvent"

@EventHandler
class ProjectAssignmentUpdatingEventHandler(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)

    @RabbitListener(queues = [PROJECT_UPDATED_QUEUE])
    fun handle(event: ProjectUpdatedEvent) {
        log.info { "Handling $event" }
        val projectId = event.project.id
        findEmployeeIds(EmployeesWhoWorkedOnProject(projectId = projectId, pageSize = PageSize.MAX))
            .onEach { log.info { "Updating project assignments for project [$projectId] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.updateProjectAssignmentsOfProject(event.project) }
            }
    }

    private fun Employee.updateProjectAssignmentsOfProject(project: Project): Employee =
        copy(projects = projects.map {
            when (it.project.id) {
                project.id -> it.copy(project = project)
                else -> it
            }
        })

}

@Configuration
class ProjectAssignmentUpdatingEventHandlerConfiguration {

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Queue")
    fun projectUpdatedEventQueue() = durableQueue(PROJECT_UPDATED_QUEUE)

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Binding")
    fun projectUpdatedEventBinding() = eventBinding<ProjectUpdatedEvent>(PROJECT_UPDATED_QUEUE)

}
