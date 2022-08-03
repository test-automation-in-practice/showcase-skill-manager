package skillmanagement.common.events

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsInOrder
import io.github.logrecorder.logback.junit5.RecordLoggers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.amqp.rabbit.core.RabbitTemplate
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class PublishEventFunctionTests {

    private val rabbitTemplate: RabbitTemplate = mockk(relaxed = true)
    private val counter: EventCounter = mockk(relaxed = true)
    private val publishEvent = PublishEventFunction(rabbitTemplate, counter)

    @Test
    fun `events are published to the exchange`() {
        publishEvent(TestEventOne)
        verify { rabbitTemplate.convertAndSend(EVENT_EXCHANGE, "TestEventOne", TestEventOne) }
    }

    @Test
    @RecordLoggers(PublishEventFunction::class)
    fun `published events are logged`(log: LogRecord) {
        listOf(TestEventOne, TestEventTwo).forEach { publishEvent(it) }

        assertThat(log) containsInOrder  {
            any("Publishing $TestEventOne")
            any("Publishing $TestEventTwo")
        }
    }

    @Test
    fun `counter is incremented whenever an event is published`() {
        publishEvent(TestEventOne)
        verify { counter.increment(TestEventOne::class) }
    }

    @Test
    fun `counter is incremented even if publishing fails`() {
        every { rabbitTemplate.convertAndSend(EVENT_EXCHANGE, "TestEventTwo", TestEventTwo) }
            .throws(RuntimeException())
        assertThrows<RuntimeException> { publishEvent(TestEventTwo) }
        verify { counter.increment(TestEventTwo::class) }
    }

}
