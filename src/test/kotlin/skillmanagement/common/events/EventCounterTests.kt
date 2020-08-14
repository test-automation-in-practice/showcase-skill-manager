package skillmanagement.common.events

import io.kotlintest.matchers.beEmpty
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Test
import skillmanagement.test.UnitTest

@UnitTest
internal class EventCounterTests {

    val registry = SimpleMeterRegistry()
    val eventCounter = EventCounter(registry)

    @Test
    fun `without any events there are no counters`() {
        findWithName().counters() should beEmpty()
    }

    @Test
    fun `with events they are counted by their type`() {
        count(EventTypeOne, EventTypeTwo, EventTypeOne)

        counterValue("EventTypeOne") shouldBe 2
        counterValue("EventTypeTwo") shouldBe 1
    }

    private fun count(vararg events: Event) = events.forEach { eventCounter.increment(it::class) }
    private fun counterValue(type: String) = counter(type)?.count()?.toInt()
    private fun counter(type: String) = findWithName().tag("type", type).counter()
    private fun findWithName() = registry.find("events.published.total")

    private object EventTypeOne : Event
    private object EventTypeTwo : Event
}
