package skillmanagement.domain.projects.searchindex

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import skillmanagement.common.events.Event
import skillmanagement.common.events.EventsTopicExchange
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.model.ProjectEvent
import skillmanagement.domain.projects.model.ProjectUpdatedEvent

private const val PREFIX = "ProjectSearchIndexUpdatingEventHandler-"

private const val QUEUE_BEAN_SUFFIX = "-Queue"
private const val BINDING_BEAN_SUFFIX = "-Binding"

private const val PROJECT_ADDED_QUEUE_NAME = PREFIX + "ProjectAddedEvent"
private const val PROJECT_UPDATED_QUEUE_NAME = PREFIX + "ProjectUpdatedEvent"
private const val PROJECT_DELETED_QUEUE_NAME = PREFIX + "ProjectDeletedEvent"

@EventHandler
class ProjectSearchIndexUpdatingEventHandler(
    private val searchIndex: ProjectSearchIndex
) {

    @Async
    @RabbitListener(queues = [PROJECT_ADDED_QUEUE_NAME])
    fun handle(event: ProjectAddedEvent) {
        searchIndex.index(event.project)
    }

    @Async
    @RabbitListener(queues = [PROJECT_UPDATED_QUEUE_NAME])
    fun handle(event: ProjectUpdatedEvent) {
        searchIndex.index(event.project)
    }

    @Async
    @RabbitListener(queues = [PROJECT_DELETED_QUEUE_NAME])
    fun handle(event: ProjectDeletedEvent) {
        searchIndex.deleteById(event.project.id)
    }

}

@Configuration
class ProjectSearchIndexUpdatingEventHandlerConfiguration(
    private val exchange: EventsTopicExchange
) {

    // project added

    @Bean(PROJECT_ADDED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun projectAddedEventQueue() = durableQueue(PROJECT_ADDED_QUEUE_NAME)

    @Bean(PROJECT_ADDED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun projectAddedEventBinding(): Binding = binding<ProjectAddedEvent>(projectAddedEventQueue())

    // project updated

    @Bean(PROJECT_UPDATED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun projectUpdatedEventQueue() = durableQueue(PROJECT_UPDATED_QUEUE_NAME)

    @Bean(PROJECT_UPDATED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun projectUpdatedEventBinding(): Binding = binding<ProjectUpdatedEvent>(projectUpdatedEventQueue())

    // project deleted

    @Bean(PROJECT_DELETED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun projectDeletedEventQueue() = durableQueue(PROJECT_DELETED_QUEUE_NAME)

    @Bean(PROJECT_DELETED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun projectDeletedEventBinding(): Binding = binding<ProjectDeletedEvent>(projectDeletedEventQueue())

    // common

    private inline infix fun <reified T : ProjectEvent> binding(queue: Queue): Binding =
        BindingBuilder.bind(queue).to(exchange).with(T::class.simpleName!!)

}
