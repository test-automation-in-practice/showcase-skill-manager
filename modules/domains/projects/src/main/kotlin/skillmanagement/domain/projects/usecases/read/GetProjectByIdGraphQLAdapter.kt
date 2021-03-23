package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project
import java.util.UUID

@GraphQLAdapter
internal class GetProjectByIdGraphQLAdapter(
    private val getProjectById: GetProjectByIdFunction
) : GraphQLQueryResolver {

    fun getProjectById(id: String): Project? =
        getProjectById(UUID.fromString(id))

}
