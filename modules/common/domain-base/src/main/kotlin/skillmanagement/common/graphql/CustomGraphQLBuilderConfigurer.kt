package skillmanagement.common.graphql

import graphql.GraphQL
import graphql.kickstart.execution.config.GraphQLBuilderConfigurer
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
internal class CustomGraphQLBuilderConfigurer : GraphQLBuilderConfigurer {

    private val log = KotlinLogging.logger {}

    override fun configure(builder: GraphQL.Builder) {
        log.debug { "applying custom GraphQL builder configuration" }
        builder.defaultDataFetcherExceptionHandler(CustomDataFetcherExceptionHandler())
    }
}
