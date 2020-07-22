package skillmanagement.domain

import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * A [MetricProvider] is a [Component] that provides one or more metrics
 * within its domain.
 *
 * These metrics can be read using either the _Spring Boot Actuator_ `metrics`
 * or `prometheus` endpoints.
 */
@Component
@Retention
@Target(CLASS)
annotation class MetricProvider
