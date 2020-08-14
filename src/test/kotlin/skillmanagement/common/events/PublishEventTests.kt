package skillmanagement.common.events

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.context.ApplicationEventPublisher
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class PublishEventTests {

    val eventPublisher: ApplicationEventPublisher = mockk(relaxed = true)
    val counter: EventCounter = mockk(relaxed = true)
    val publishEvent = PublishEvent(eventPublisher, counter)

    val event = mockk<Event>()

    @Test
    fun `events are published as application events`() {
        publishEvent(event)
        verify { eventPublisher.publishEvent(event) }
    }

    @Test
    @RecordLoggers(PublishEvent::class)
    fun `events publishing is logged`(log: LogRecord) {
        publishEvent(event)
        assertThat(log.messages).containsExactly("Publishing $event")
    }

    @Test
    fun `counter is incremented whenever an event is published`() {
        publishEvent(event)
        verify { counter.increment(event::class) }
    }

    @Test
    fun `counter is incremented even if publishing fails`() {
        every { eventPublisher.publishEvent(event) } throws RuntimeException()
        assertThrows<RuntimeException> { publishEvent(event) }
        verify { counter.increment(event::class) }
    }

}
