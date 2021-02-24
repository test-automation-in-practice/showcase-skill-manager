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
internal class PublishEventTests {

    private val rabbitTemplate: RabbitTemplate = mockk(relaxed = true)
    private val exchange = EventsTopicExchange()
    private val counter: EventCounter = mockk(relaxed = true)
    private val publishEvent = PublishEvent(rabbitTemplate, exchange, counter)

    @Test
    fun `events are published to the exchange`() {
        publishEvent(TestEventOne)
        verify { rabbitTemplate.convertAndSend("events", "TestEventOne", TestEventOne) }
    }

    @Test
    @RecordLoggers(PublishEvent::class)
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
        every { rabbitTemplate.convertAndSend("events", "TestEventTwo", TestEventTwo) }
            .throws(RuntimeException())
        assertThrows<RuntimeException> { publishEvent(TestEventTwo) }
        verify { counter.increment(TestEventTwo::class) }
    }

}
