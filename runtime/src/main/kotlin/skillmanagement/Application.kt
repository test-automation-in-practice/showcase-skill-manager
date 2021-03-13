package skillmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.util.IdGenerator
import skillmanagement.common.CommonConfiguration
import skillmanagement.domain.employees.EmployeesDomainConfiguration
import skillmanagement.domain.projects.ProjectsDomainConfiguration
import skillmanagement.domain.skills.SkillsDomainConfiguration
import java.time.Clock

@EnableAsync
@EnableRetry
@EnableScheduling
@EnableHypermediaSupport(type = [HAL])
@SpringBootApplication
@ConfigurationPropertiesScan
class Application

/**
 * Provides commonly used components.
 *
 * @see Clock
 * @see IdGenerator
 */
@Configuration
@Import(
    CommonConfiguration::class,
    EmployeesDomainConfiguration::class,
    ProjectsDomainConfiguration::class,
    SkillsDomainConfiguration::class
)
class ApplicationConfiguration

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
