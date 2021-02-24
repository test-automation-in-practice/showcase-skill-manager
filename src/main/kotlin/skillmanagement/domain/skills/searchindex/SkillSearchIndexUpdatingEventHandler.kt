package skillmanagement.domain.skills.searchindex

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.messaging.QUEUE_PREFIX
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.messaging.eventBinding
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.SkillUpdatedEvent

private const val CONTEXT = "SkillSearchIndexUpdatingEventHandler"

private const val SKILL_ADDED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillAddedEvent"
private const val SKILL_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillUpdatedEvent"
private const val SKILL_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillDeletedEvent"

@EventHandler
class SkillSearchIndexUpdatingEventHandler(
    private val searchIndex: SkillSearchIndex
) {

    private val log = logger {}

    @RabbitListener(queues = [SKILL_ADDED_QUEUE])
    fun handle(event: SkillAddedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.skill)
    }

    @RabbitListener(queues = [SKILL_UPDATED_QUEUE])
    fun handle(event: SkillUpdatedEvent) {
        log.debug { "Handling $event" }
        searchIndex.index(event.skill)
    }

    @RabbitListener(queues = [SKILL_DELETED_QUEUE])
    fun handle(event: SkillDeletedEvent) {
        log.debug { "Handling $event" }
        searchIndex.deleteById(event.skill.id)
    }

}

@Configuration
class SkillSearchIndexUpdatingEventHandlerConfiguration {

    @Bean("$CONTEXT.SkillAddedEvent.Queue")
    fun skillAddedEventQueue() = durableQueue(SKILL_ADDED_QUEUE)

    @Bean("$CONTEXT.SkillAddedEvent.Binding")
    fun skillAddedEventBinding() = eventBinding<SkillAddedEvent>(SKILL_ADDED_QUEUE)

    @Bean("$CONTEXT.SkillUpdatedEvent.Queue")
    fun skillUpdatedEventQueue() = durableQueue(SKILL_UPDATED_QUEUE)

    @Bean("$CONTEXT.SkillUpdatedEvent.Binding")
    fun skillUpdatedEventBinding() = eventBinding<SkillUpdatedEvent>(SKILL_UPDATED_QUEUE)

    @Bean("$CONTEXT.SkillDeletedEvent.Queue")
    fun skillDeletedEventQueue() = durableQueue(SKILL_DELETED_QUEUE)

    @Bean("$CONTEXT.SkillDeletedEvent.Binding")
    fun skillDeletedEventBinding() = eventBinding<SkillDeletedEvent>(SKILL_DELETED_QUEUE)

}
