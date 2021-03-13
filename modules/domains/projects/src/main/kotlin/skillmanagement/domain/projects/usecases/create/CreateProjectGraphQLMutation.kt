package skillmanagement.domain.projects.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel

@Component
internal class CreateProjectGraphQLMutation(
    private val createProject: CreateProjectFunction
) : GraphQLMutationResolver {

    fun createProject(label: String, description: String): Project = withErrorHandling {
        createProject(
            label = ProjectLabel(label),
            description = ProjectDescription(description)
        )
    }

}
