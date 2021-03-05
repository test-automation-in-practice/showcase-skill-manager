package skillmanagement.domain.projects.searchindex

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.messaging.QUEUE_PREFIX
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.messaging.eventBinding
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.model.ProjectUpdatedEvent

private const val CONTEXT = "ProjectSearchIndexUpdatingEventHandler"

private const val PROJECT_ADDED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectAddedEvent"
private const val PROJECT_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectUpdatedEvent"
private const val PROJECT_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.ProjectDeletedEvent"

@EventHandler
internal class ProjectSearchIndexUpdatingEventHandler(
    private val searchIndex: ProjectSearchIndex
) {

    private val log = logger {}

    @RabbitListener(queues = [PROJECT_ADDED_QUEUE])
    fun handle(event: ProjectAddedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.project)
    }

    @RabbitListener(queues = [PROJECT_UPDATED_QUEUE])
    fun handle(event: ProjectUpdatedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.project)
    }

    @RabbitListener(queues = [PROJECT_DELETED_QUEUE])
    fun handle(event: ProjectDeletedEvent) {
        log.debug { "Handling $event" }
        searchIndex.deleteById(event.project.id)
    }

}

@Configuration
internal class ProjectSearchIndexUpdatingEventHandlerConfiguration {

    @Bean("$CONTEXT.ProjectAddedEvent.Queue")
    fun projectAddedEventQueue() = durableQueue(PROJECT_ADDED_QUEUE)

    @Bean("$CONTEXT.ProjectAddedEvent.Binding")
    fun projectAddedEventBinding() = eventBinding<ProjectAddedEvent>(PROJECT_ADDED_QUEUE)

    @Bean("$CONTEXT.ProjectUpdatedEvent.Queue")
    fun projectUpdatedEventQueue() = durableQueue(PROJECT_UPDATED_QUEUE)

    @Bean("$CONTEXT.ProjectUpdatedEvent.Binding")
    fun projectUpdatedEventBinding() = eventBinding<ProjectUpdatedEvent>(PROJECT_UPDATED_QUEUE)

    @Bean("$CONTEXT.ProjectDeletedEvent.Queue")
    fun projectDeletedEventQueue() = durableQueue(PROJECT_DELETED_QUEUE)

    @Bean("$CONTEXT.ProjectDeletedEvent.Binding")
    fun projectDeletedEventBinding() = eventBinding<ProjectDeletedEvent>(PROJECT_DELETED_QUEUE)

}
