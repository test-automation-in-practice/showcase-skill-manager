package skillmanagement.common.events

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Binding.DestinationType.QUEUE
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import kotlin.reflect.KClass

internal const val EXCHANGE_PREFIX = "skillmanager.exchanges"
const val QUEUE_PREFIX = "skillmanager.queues"

internal const val EVENT_EXCHANGE = "$EXCHANGE_PREFIX.events"

fun durableQueue(name: String): Queue = QueueBuilder.durable(name)
    .withArgument(DEAD_LETTER_EXCHANGE_HEADER, DEAD_LETTER_EXCHANGE)
    .build()

inline fun <reified T : Any> eventBinding(queueName: String) = eventBinding(queueName, T::class)

fun <T : Any> eventBinding(queueName: String, eventType: KClass<T>): Binding =
    Binding(queueName, QUEUE, EVENT_EXCHANGE, routingKey(eventType), emptyMap())

internal fun <T : Any> routingKey(eventType: KClass<T>) = "${eventType.simpleName}"
