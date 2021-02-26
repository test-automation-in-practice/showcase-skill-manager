package skillmanagement.common.events

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.test.UnitTest
import kotlin.reflect.KClass

@UnitTest
internal class EventCounterTests {

    private val registry = SimpleMeterRegistry()
    private val eventCounter = EventCounter(registry)

    @Test
    fun `without any events there is no counter`() {
        assertThat(getCountersWitMetricName()).isEmpty()
    }

    @Test
    fun `with events they are counted by their type`() {
        count(TestEventOne, TestEventTwo, TestEventOne)
        assertThat(counterValueOf(TestEventOne::class)).isEqualTo(2)
        assertThat(counterValueOf(TestEventTwo::class)).isEqualTo(1)
    }

    private fun count(vararg events: TestEvent) =
        events.forEach { eventCounter.increment(it::class) }

    private fun counterValueOf(type: KClass<out TestEvent>) =
        registry.find(eventCounter.name)
            .tag("type", type.simpleName!!)
            .counter()?.count()?.toInt()

    private fun getCountersWitMetricName() =
        registry.find(eventCounter.name).counters()

}
