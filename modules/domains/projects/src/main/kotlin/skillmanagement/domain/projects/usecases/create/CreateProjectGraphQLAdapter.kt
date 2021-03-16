package skillmanagement.domain.projects.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel

@GraphQLAdapter
internal class CreateProjectGraphQLAdapter(
    private val createProject: CreateProjectFunction
) : GraphQLMutationResolver {

    fun createProject(input: ProjectInput): Project = withErrorHandling {
        createProject(
            label = ProjectLabel(input.label),
            description = ProjectDescription(input.description)
        )
    }

    data class ProjectInput(
        val label: String,
        val description: String
    )

}
