package skillmanagement.common.events

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import skillmanagement.common.stereotypes.TechnicalFunction

/**
 * Used to publish [events][Event] to [event topic][EventsTopicExchange].
 *
 * In difference to just using the [EventsTopicExchange] directly,
 * this function will also increment the [EventCounter] for each published
 * event.
 */
@TechnicalFunction
class PublishEventFunction(
    private val rabbitTemplate: RabbitTemplate,
    private val counter: EventCounter
) {

    private val log = logger {}

    operator fun invoke(event: Event) {
        log.debug { "Publishing $event" }
        counter.increment(event::class)
        rabbitTemplate.convertAndSend(EVENT_EXCHANGE, routingKey(event::class), event)
    }

}
