package skillmanagement.test.events

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.EventsConfiguration
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.e2e.PROPERTY_DOCKERIZED_BROKER_HOST
import skillmanagement.test.e2e.PROPERTY_DOCKERIZED_BROKER_PORT
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@TechnologyIntegrationTest
@RunWithDockerizedRabbitMq
@SpringBootTest(
    classes = [EventingIntegrationTestConfiguration::class],
    properties = [PROPERTY_DOCKERIZED_BROKER_HOST, PROPERTY_DOCKERIZED_BROKER_PORT]
)
annotation class EventingSpringIntegrationTest

@Import(
    EventsConfiguration::class,
    RabbitAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
internal class EventingIntegrationTestConfiguration {
    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()
}
