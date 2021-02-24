package skillmanagement.common.events

import org.springframework.amqp.core.TopicExchange
import org.springframework.stereotype.Component

@Component
class EventsTopicExchange : TopicExchange("events")
