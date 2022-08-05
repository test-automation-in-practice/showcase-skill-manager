package skillmanagement.domain.employees

import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import skillmanagement.common.CommonGraphQlConfiguration
import skillmanagement.common.graphql.IntTypeCoercing
import skillmanagement.common.graphql.scalarType
import skillmanagement.domain.employees.model.SkillLevel

@Configuration
@Import(CommonGraphQlConfiguration::class)
class EmployeesDomainGraphQlConfiguration : RuntimeWiringConfigurer {

    private object SkillLevelCoercing : IntTypeCoercing<SkillLevel>() {
        override val typeName = "SkillLevel"
        override fun createInstance(value: Int) = SkillLevel(value)
    }

    override fun configure(builder: RuntimeWiring.Builder) {
        builder.scalar(scalarType("SkillLevel", SkillLevelCoercing))
    }

}
