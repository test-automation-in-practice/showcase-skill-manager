package skillmanagement.common.events

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
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
        assertThat(log.messages)
            .containsExactly("Publishing $TestEventOne", "Publishing $TestEventTwo")
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
