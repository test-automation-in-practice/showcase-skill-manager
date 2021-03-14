package skillmanagement.domain.employees.usecases.projectassignments.update

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.events.QUEUE_PREFIX
import skillmanagement.common.events.durableQueue
import skillmanagement.common.events.eventBinding
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.ProjectUpdatedEvent
import skillmanagement.domain.employees.usecases.read.EmployeesWhoWorkedOnProject
import skillmanagement.domain.employees.usecases.read.GetEmployeeIdsFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction

private const val CONTEXT = "ProjectAssignmentUpdatingEventHandler"
private const val PROJECT_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectUpdatedEvent"

@EventHandler
internal class ProjectAssignmentUpdatingEventHandler(
    private val getEmployeeIds: GetEmployeeIdsFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)

    @RabbitListener(queues = [PROJECT_UPDATED_QUEUE])
    fun handle(event: ProjectUpdatedEvent) {
        log.debug { "Handling $event" }
        val projectId = event.project.id
        getEmployeeIds(EmployeesWhoWorkedOnProject(projectId = projectId, pageSize = PageSize.MAX))
            .onEach { log.info { "Updating project assignments for project [$projectId] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.updateProjectAssignmentsOfProject(event.project) }
            }
    }

    private fun Employee.updateProjectAssignmentsOfProject(project: ProjectData): Employee =
        copy(projects = projects.map {
            when (it.project.id) {
                project.id -> it.copy(project = project)
                else -> it
            }
        })

}

@Configuration
internal class ProjectAssignmentUpdatingEventHandlerConfiguration {

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Queue")
    fun projectUpdatedEventQueue() = durableQueue(PROJECT_UPDATED_QUEUE)

    @Bean("${CONTEXT}.ProjectUpdatedEvent.Binding")
    fun projectUpdatedEventBinding() = eventBinding<ProjectUpdatedEvent>(PROJECT_UPDATED_QUEUE)

}
