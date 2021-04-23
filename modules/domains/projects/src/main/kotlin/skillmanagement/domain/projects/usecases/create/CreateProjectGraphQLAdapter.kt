package skillmanagement.domain.projects.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectRepresentation
import skillmanagement.domain.projects.model.toRepresentation

@GraphQLAdapter
internal class CreateProjectGraphQLAdapter(
    private val createProjectFunction: CreateProjectFunction
) : GraphQLMutationResolver {

    fun createProject(input: ProjectCreationData): ProjectRepresentation =
        createProjectFunction(input).toRepresentation()

}
