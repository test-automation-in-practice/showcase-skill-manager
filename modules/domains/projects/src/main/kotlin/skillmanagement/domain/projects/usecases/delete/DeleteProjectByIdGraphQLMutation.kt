package skillmanagement.domain.projects.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import java.util.UUID

@Component
internal class DeleteProjectByIdGraphQLMutation(
    private val deleteProjectById: DeleteProjectByIdFunction
) : GraphQLMutationResolver {

    fun deleteProjectById(id: String): Boolean = withErrorHandling {
        when (deleteProjectById(UUID.fromString(id))) {
            ProjectNotFound -> false
            SuccessfullyDeleted -> true
        }
    }

}
