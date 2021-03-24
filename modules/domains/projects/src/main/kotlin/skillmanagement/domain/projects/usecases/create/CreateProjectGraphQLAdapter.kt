package skillmanagement.domain.projects.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectCreationData

@GraphQLAdapter
internal class CreateProjectGraphQLAdapter(
    private val createProjectFunction: CreateProjectFunction
) : GraphQLMutationResolver {

    fun createProject(input: ProjectCreationData): Project = createProjectFunction(input)

}
