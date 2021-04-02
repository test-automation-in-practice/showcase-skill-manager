package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.projectId

@GraphQLAdapter
internal class GetProjectByIdGraphQLAdapter(
    private val getProjectById: GetProjectByIdFunction
) : GraphQLQueryResolver {

    fun getProjectById(id: String): Project? = getProjectById(projectId(id))

}
