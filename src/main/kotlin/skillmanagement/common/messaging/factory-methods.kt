package skillmanagement.common.messaging

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Binding.DestinationType.QUEUE
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import skillmanagement.common.events.Event
import kotlin.reflect.KClass

internal const val EXCHANGE_PREFIX = "skillmanager.exchanges"
internal const val QUEUE_PREFIX = "skillmanager.queues"

internal const val EVENT_EXCHANGE = "$EXCHANGE_PREFIX.events"

fun durableQueue(name: String): Queue = QueueBuilder.durable(name)
    .withArgument(DEAD_LETTER_EXCHANGE_HEADER, DEAD_LETTER_EXCHANGE)
    .build()

inline fun <reified T : Event> eventBinding(queueName: String) = eventBinding(queueName, T::class)

fun <T : Event> eventBinding(queueName: String, eventType: KClass<T>): Binding =
    Binding(queueName, QUEUE, EVENT_EXCHANGE, routingKey(eventType), emptyMap())

fun <T : Event> routingKey(eventType: KClass<T>) = "${eventType.simpleName}"
