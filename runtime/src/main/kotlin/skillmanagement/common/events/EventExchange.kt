package skillmanagement.common.events

import org.springframework.amqp.core.TopicExchange
import org.springframework.stereotype.Component
import skillmanagement.common.messaging.EVENT_EXCHANGE

@Component
class EventExchange : TopicExchange(EVENT_EXCHANGE)