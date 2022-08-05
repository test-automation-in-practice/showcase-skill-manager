package skillmanagement.domain.skills

import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import skillmanagement.common.CommonGraphQlConfiguration

@Configuration
@Import(CommonGraphQlConfiguration::class)
class SkillsDomainGraphQlConfiguration : RuntimeWiringConfigurer {

    override fun configure(builder: RuntimeWiring.Builder) {
        // currently nothing to add
    }

}
