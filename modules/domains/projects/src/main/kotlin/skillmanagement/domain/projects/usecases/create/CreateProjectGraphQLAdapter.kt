package skillmanagement.domain.projects.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel

@GraphQLAdapter
internal class CreateProjectGraphQLAdapter(
    private val createProject: CreateProjectFunction
) : GraphQLMutationResolver {

    fun createProject(input: ProjectInput): Project =
        createProject(input.label, input.description)

    data class ProjectInput(
        val label: ProjectLabel,
        val description: ProjectDescription
    )

}
