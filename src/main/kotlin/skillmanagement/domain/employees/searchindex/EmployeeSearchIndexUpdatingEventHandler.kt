package skillmanagement.domain.employees.searchindex

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import skillmanagement.common.events.EventsTopicExchange
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.EmployeeAddedEvent
import skillmanagement.domain.employees.model.EmployeeDeletedEvent
import skillmanagement.domain.employees.model.EmployeeEvent
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent

private const val PREFIX = "EmployeeSearchIndexUpdatingEventHandler-"

private const val QUEUE_BEAN_SUFFIX = "-Queue"
private const val BINDING_BEAN_SUFFIX = "-Binding"

private const val EMPLOYEE_ADDED_QUEUE_NAME = PREFIX + "EmployeeAddedEvent"
private const val EMPLOYEE_UPDATED_QUEUE_NAME = PREFIX + "EmployeeUpdatedEvent"
private const val EMPLOYEE_DELETED_QUEUE_NAME = PREFIX + "EmployeeDeletedEvent"

@EventHandler
class EmployeeSearchIndexUpdatingEventHandler(
    private val searchIndex: EmployeeSearchIndex
) {

    @Async
    @RabbitListener(queues = [EMPLOYEE_ADDED_QUEUE_NAME])
    fun handle(event: EmployeeAddedEvent) {
        searchIndex.index(event.employee)
    }

    @Async
    @RabbitListener(queues = [EMPLOYEE_UPDATED_QUEUE_NAME])
    fun handle(event: EmployeeUpdatedEvent) {
        searchIndex.index(event.employee)
    }

    @Async
    @RabbitListener(queues = [EMPLOYEE_DELETED_QUEUE_NAME])
    fun handle(event: EmployeeDeletedEvent) {
        searchIndex.deleteById(event.employee.id)
    }

}

@Configuration
class EmployeeSearchIndexUpdatingEventHandlerConfiguration(
    private val exchange: EventsTopicExchange
) {

    // employee added

    @Bean(EMPLOYEE_ADDED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun employeeAddedEventQueue() = durableQueue(EMPLOYEE_ADDED_QUEUE_NAME)

    @Bean(EMPLOYEE_ADDED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun employeeAddedEventBinding() = binding<EmployeeAddedEvent>(employeeAddedEventQueue())

    // employee updated

    @Bean(EMPLOYEE_UPDATED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun employeeUpdatedEventQueue() = durableQueue(EMPLOYEE_UPDATED_QUEUE_NAME)

    @Bean(EMPLOYEE_UPDATED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun employeeUpdatedEventBinding() = binding<EmployeeUpdatedEvent>(employeeUpdatedEventQueue())

    // employee deleted

    @Bean(EMPLOYEE_DELETED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun employeeDeletedEventQueue() = durableQueue(EMPLOYEE_DELETED_QUEUE_NAME)

    @Bean(EMPLOYEE_DELETED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun employeeDeletedEventBinding() = binding<EmployeeDeletedEvent>(employeeDeletedEventQueue())

    // common

    private fun durableQueue(name: String): Queue = Queue(name, true)

    private inline infix fun <reified T : EmployeeEvent> binding(queue: Queue): Binding =
        BindingBuilder.bind(queue).to(exchange).with(T::class.simpleName!!)

}
