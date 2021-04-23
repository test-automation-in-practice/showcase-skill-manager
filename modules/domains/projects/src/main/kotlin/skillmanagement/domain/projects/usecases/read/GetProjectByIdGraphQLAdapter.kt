package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectRepresentation
import skillmanagement.domain.projects.model.projectId
import skillmanagement.domain.projects.model.toRepresentation

@GraphQLAdapter
internal class GetProjectByIdGraphQLAdapter(
    private val getProjectById: GetProjectByIdFunction
) : GraphQLQueryResolver {

    fun getProjectById(id: String): ProjectRepresentation? =
        getProjectById(projectId(id))?.toRepresentation()

}
