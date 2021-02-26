package skillmanagement.domain.skills

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import skillmanagement.common.CommonConfiguration

@Configuration
@ComponentScan
@Import(CommonConfiguration::class)
class SkillsDomainConfiguration
