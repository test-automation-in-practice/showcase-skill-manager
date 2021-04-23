package skillmanagement.domain.employees.usecases.projectassignments.update

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.events.QUEUE_PREFIX
import skillmanagement.common.events.durableQueue
import skillmanagement.common.events.eventBinding
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.ProjectUpdatedEvent
import skillmanagement.domain.employees.usecases.read.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.read.GetEmployeeIdsFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction

private const val CONTEXT = "ProjectAssignmentUpdatingEventHandler"
private const val PROJECT_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectUpdatedEvent"

@EventHandler
internal class ProjectAssignmentUpdatingEventHandler(
    private val getEmployeeIds: GetEmployeeIdsFunction,
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)
    // TODO: log update failures?

    @RabbitListener(queues = [PROJECT_UPDATED_QUEUE])
    fun handle(event: ProjectUpdatedEvent) {
        log.debug { "Handling $event" }
        val project = event.project
        getEmployeeIds(EmployeesWhoWorkedOnProject(project.id, Pagination(size = PageSize.MAX)))
            .onEach { log.info { "Updating project assignments for project [${project.id}] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeEntityById(employeeId) { employee ->
                    employee.updateProjectAssignment(project.id) { assignment ->
                        assignment.copy(project = project)
                    }
                }
            }
    }

}

@Configuration
internal class ProjectAssignmentUpdatingEventHandlerConfiguration {

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Queue")
    fun projectUpdatedEventQueue() = durableQueue(PROJECT_UPDATED_QUEUE)

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Binding")
    fun projectUpdatedEventBinding() = eventBinding<ProjectUpdatedEvent>(PROJECT_UPDATED_QUEUE)

}
