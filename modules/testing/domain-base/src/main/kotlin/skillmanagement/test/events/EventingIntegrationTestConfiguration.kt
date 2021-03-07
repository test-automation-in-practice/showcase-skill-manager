package skillmanagement.test.events

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.EventsConfiguration

@Import(
    EventsConfiguration::class,
    RabbitAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
class EventingIntegrationTestConfiguration {
    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()
}