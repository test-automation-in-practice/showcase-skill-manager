package skillmanagement.test.events

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.EventsConfiguration
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.docker.WaitForAllContainersToStart
import skillmanagement.test.e2e.PROPERTY_DOCKERIZED_BROKER_HOST
import skillmanagement.test.e2e.PROPERTY_DOCKERIZED_BROKER_PORT
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * This kind of [TechnologyIntegrationTest] bootstraps everything necessary
 * for testing event related code:
 *
 * - Dockerized RabbitMQ as the broker
 * - Import of [EventsConfiguration]
 *    - object to message mapping
 *    - event topic exchange
 *    - dead letter topic & queues
 * - Import of [PublishEventFunction]
 */
@Retention
@Target(CLASS)
@TechnologyIntegrationTest
@RunWithDockerizedRabbitMq
@WaitForAllContainersToStart
@SpringBootTest(
    classes = [EventingIntegrationTestConfiguration::class],
    properties = [PROPERTY_DOCKERIZED_BROKER_HOST, PROPERTY_DOCKERIZED_BROKER_PORT]
)
annotation class EventingSpringIntegrationTest

@Import(
    EventsConfiguration::class,
    PublishEventFunction::class,
    RabbitAutoConfiguration::class,
    JacksonAutoConfiguration::class
)
private class EventingIntegrationTestConfiguration {
    @Bean
    fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()
}
