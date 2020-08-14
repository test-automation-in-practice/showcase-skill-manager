package skillmanagement.common.events

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import skillmanagement.common.stereotypes.Metric
import kotlin.reflect.KClass

@Metric
class EventCounter(
    private val registry: MeterRegistry
) {

    fun increment(eventClass: KClass<out Event>) {
        registry.counter("events.published.total", tags(eventClass)).increment()
    }

    private fun tags(eventClass: KClass<out Event>) =
        listOf(Tag.of("type", eventClass.simpleName ?: "unknown"))

}
