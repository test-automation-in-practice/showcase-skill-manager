package skillmanagement.domain.employees.usecases.projectassignments.delete

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
import skillmanagement.domain.employees.model.ProjectDeletedEvent
import skillmanagement.domain.employees.usecases.find.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.find.FindEmployeeIds
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import java.util.UUID

private const val CONTEXT = "ProjectAssignmentDeletingEventHandler"
private const val PROJECT_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectDeletedEvent"

@EventHandler
class ProjectAssignmentDeletingEventHandler(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)

    @RabbitListener(queues = [PROJECT_DELETED_QUEUE])
    fun handle(event: ProjectDeletedEvent) {
        log.debug { "Handling $event" }
        val projectId = event.project.id
        findEmployeeIds(EmployeesWhoWorkedOnProject(projectId = projectId, pageSize = PageSize.MAX))
            .onEach { log.info { "Removing projects assignments of project [$projectId] from employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.removeProjectAssignmentsByProjectId(projectId) }
            }
    }

    private fun Employee.removeProjectAssignmentsByProjectId(projectId: UUID): Employee =
        copy(projects = projects.filter { it.project.id != projectId })

}

@Configuration
class ProjectAssignmentDeletingEventHandlerConfiguration {

    @Bean("$CONTEXT.ProjectDeletedEvent.Queue")
    fun projectDeletedEventQueue() = durableQueue(PROJECT_DELETED_QUEUE)

    @Bean("$CONTEXT.ProjectDeletedEvent.Binding")
    fun projectDeletedEventBinding() = eventBinding<ProjectDeletedEvent>(PROJECT_DELETED_QUEUE)

}
