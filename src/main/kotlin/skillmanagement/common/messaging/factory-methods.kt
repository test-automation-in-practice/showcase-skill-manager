package skillmanagement.common.messaging

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder

fun durableQueue(name: String): Queue = QueueBuilder.durable(name)
    .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME)
    .build()
