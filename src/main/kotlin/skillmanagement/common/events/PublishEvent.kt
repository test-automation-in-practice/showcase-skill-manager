package skillmanagement.common.events

import mu.KotlinLogging.logger
import org.springframework.context.ApplicationEventPublisher
import skillmanagement.common.stereotypes.TechnicalFunction

@TechnicalFunction
class PublishEvent(
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    private val log = logger {}

    operator fun invoke(event: Event) {
        log.info { "Publishing $event" }
        applicationEventPublisher.publishEvent(event)
    }

}
