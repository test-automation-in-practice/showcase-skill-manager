package skillmanagement.common

import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import skillmanagement.common.graphql.IntTypeCoercing
import skillmanagement.common.graphql.scalarType
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.searchindices.MaxSuggestions

@Configuration
class CommonGraphQlConfiguration : RuntimeWiringConfigurer {

    private object PageIndexCoercing : IntTypeCoercing<PageIndex>() {
        override val typeName = "PageIndex"
        override fun createInstance(value: Int) = PageIndex(value)
    }

    private object PageSizeCoercing : IntTypeCoercing<PageSize>() {
        override val typeName = "PageSize"
        override fun createInstance(value: Int) = PageSize(value)
    }

    private object MaxSuggestionsCoercing : IntTypeCoercing<MaxSuggestions>() {
        override val typeName = "MaxSuggestions"
        override fun createInstance(value: Int) = MaxSuggestions(value)
    }

    override fun configure(builder: RuntimeWiring.Builder) {
        builder.scalar(scalarType("PageIndex", PageIndexCoercing))
        builder.scalar(scalarType("PageSize", PageSizeCoercing))
        builder.scalar(scalarType("MaxSuggestions", MaxSuggestionsCoercing))
    }

}
