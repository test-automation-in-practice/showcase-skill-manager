package skillmanagement.domain.skills.searchindex

import mu.KotlinLogging.logger
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import skillmanagement.common.events.EventsTopicExchange
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.SkillEvent
import skillmanagement.domain.skills.model.SkillUpdatedEvent

private const val PREFIX = "SkillSearchIndexUpdatingEventHandler-"

private const val QUEUE_BEAN_SUFFIX = "-Queue"
private const val BINDING_BEAN_SUFFIX = "-Binding"

private const val SKILL_ADDED_QUEUE_NAME = PREFIX + "SkillAddedEvent"
private const val SKILL_UPDATED_QUEUE_NAME = PREFIX + "SkillUpdatedEvent"
private const val SKILL_DELETED_QUEUE_NAME = PREFIX + "SkillDeletedEvent"

@EventHandler
class SkillSearchIndexUpdatingEventHandler(
    private val searchIndex: SkillSearchIndex
) {

    private val log = logger {}

    @Async
    @RabbitListener(queues = [SKILL_ADDED_QUEUE_NAME])
    fun handle(event: SkillAddedEvent) {
        log.debug { "received [$event]" }
        searchIndex.index(event.skill)
    }

    @Async
    @RabbitListener(queues = [SKILL_UPDATED_QUEUE_NAME])
    fun handle(event: SkillUpdatedEvent) {
        log.debug { "received [$event]" }
        searchIndex.index(event.skill)
    }

    @Async
    @RabbitListener(queues = [SKILL_DELETED_QUEUE_NAME])
    fun handle(event: SkillDeletedEvent) {
        log.debug { "received [$event]" }
        searchIndex.deleteById(event.skill.id)
    }

}

@Configuration
class SkillSearchIndexUpdatingEventHandlerConfiguration(
    private val exchange: EventsTopicExchange
) {

    // skill added

    @Bean(SKILL_ADDED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun skillAddedEventQueue() = durableQueue(SKILL_ADDED_QUEUE_NAME)

    @Bean(SKILL_ADDED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun skillAddedEventBinding() = binding<SkillAddedEvent>(skillAddedEventQueue())

    // skill updated

    @Bean(SKILL_UPDATED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun skillUpdatedEventQueue() = durableQueue(SKILL_UPDATED_QUEUE_NAME)

    @Bean(SKILL_UPDATED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun skillUpdatedEventBinding() = binding<SkillUpdatedEvent>(skillUpdatedEventQueue())

    // skill deleted

    @Bean(SKILL_DELETED_QUEUE_NAME + QUEUE_BEAN_SUFFIX)
    fun skillDeletedEventQueue() = durableQueue(SKILL_DELETED_QUEUE_NAME)

    @Bean(SKILL_DELETED_QUEUE_NAME + BINDING_BEAN_SUFFIX)
    fun skillDeletedEventBinding() = binding<SkillDeletedEvent>(skillDeletedEventQueue())

    // common

    private inline infix fun <reified T : SkillEvent> binding(queue: Queue): Binding =
        BindingBuilder.bind(queue).to(exchange).with(T::class.simpleName!!)

}
