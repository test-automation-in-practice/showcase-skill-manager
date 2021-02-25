package skillmanagement.domain.employees.searchindex

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.messaging.QUEUE_PREFIX
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.messaging.eventBinding
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.EmployeeAddedEvent
import skillmanagement.domain.employees.model.EmployeeDeletedEvent
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent

private const val CONTEXT = "EmployeeSearchIndexUpdatingEventHandler"

private const val EMPLOYEE_ADDED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.EmployeeAddedEvent"
private const val EMPLOYEE_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.EmployeeUpdatedEvent"
private const val EMPLOYEE_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.EmployeeDeletedEvent"

@EventHandler
class EmployeeSearchIndexUpdatingEventHandler(
    private val searchIndex: EmployeeSearchIndex
) {

    private val log = logger {}

    @RabbitListener(queues = [EMPLOYEE_ADDED_QUEUE])
    fun handle(event: EmployeeAddedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.employee)
    }

    @RabbitListener(queues = [EMPLOYEE_UPDATED_QUEUE])
    fun handle(event: EmployeeUpdatedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.employee)
    }

    @RabbitListener(queues = [EMPLOYEE_DELETED_QUEUE])
    fun handle(event: EmployeeDeletedEvent) {
        log.debug { "Handling $event" }
        searchIndex.deleteById(event.employee.id)
    }

}

@Configuration
class EmployeeSearchIndexUpdatingEventHandlerConfiguration {

    @Bean("$CONTEXT.EmployeeAddedEvent.Queue")
    fun employeeAddedEventQueue() = durableQueue(EMPLOYEE_ADDED_QUEUE)

    @Bean("$CONTEXT.EmployeeAddedEvent.Binding")
    fun employeeAddedEventBinding() = eventBinding<EmployeeAddedEvent>(EMPLOYEE_ADDED_QUEUE)

    @Bean("$CONTEXT.EmployeeUpdatedEvent.Queue")
    fun employeeUpdatedEventQueue() = durableQueue(EMPLOYEE_UPDATED_QUEUE)

    @Bean("$CONTEXT.EmployeeUpdatedEvent.Binding")
    fun employeeUpdatedEventBinding() = eventBinding<EmployeeUpdatedEvent>(EMPLOYEE_UPDATED_QUEUE)

    @Bean("$CONTEXT.EmployeeDeletedEvent.Queue")
    fun employeeDeletedEventQueue() = durableQueue(EMPLOYEE_DELETED_QUEUE)

    @Bean("$CONTEXT.EmployeeDeletedEvent.Binding")
    fun employeeDeletedEventBinding() = eventBinding<EmployeeDeletedEvent>(EMPLOYEE_DELETED_QUEUE)

}
