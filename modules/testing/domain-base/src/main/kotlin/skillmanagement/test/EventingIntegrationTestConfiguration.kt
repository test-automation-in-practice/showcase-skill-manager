package skillmanagement.test

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.configuration.RabbitConfiguration
import skillmanagement.common.events.EventCounter
import skillmanagement.common.events.EventExchange
import skillmanagement.common.events.PublishEvent

@Import(
    PublishEvent::class,
    EventExchange::class,
    RabbitConfiguration::class,

    RabbitAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
class EventingIntegrationTestConfiguration {
    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()

    @Bean
    fun eventCounter(registry: MeterRegistry): EventCounter = EventCounter(registry)
}