package skillmanagement.common.events

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import skillmanagement.common.stereotypes.Metric
import kotlin.reflect.KClass

/**
 * This [Metric] keeps track of all published events by counting them.
 *
 * It is published under the name `events.published.total` and has one dimension
 * (_tag_) `type` which reflects the type (_class_) of event that was published.
 */
@Metric
class EventCounter(
    private val registry: MeterRegistry
) {

    val name = "events.published.total"

    fun increment(eventClass: KClass<out Event>) {
        registry.counter(name, tags(eventClass)).increment()
    }

    private fun tags(eventClass: KClass<out Event>) =
        listOf(Tag.of("type", eventClass.simpleName ?: "unknown"))

}
