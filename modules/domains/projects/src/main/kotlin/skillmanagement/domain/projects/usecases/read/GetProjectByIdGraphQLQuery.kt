package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.domain.projects.model.Project
import java.util.UUID

@Component
class GetProjectByIdGraphQLQuery(
    private val getProjectById: GetProjectByIdFunction
) : GraphQLQueryResolver {

    fun getProjectById(id: String): Project? = withErrorHandling {
        getProjectById(UUID.fromString(id))
    }

}
