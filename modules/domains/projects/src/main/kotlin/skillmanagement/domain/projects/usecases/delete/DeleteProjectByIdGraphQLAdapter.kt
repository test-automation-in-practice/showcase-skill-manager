package skillmanagement.domain.projects.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import java.util.UUID

@GraphQLAdapter
internal class DeleteProjectByIdGraphQLAdapter(
    private val deleteProjectById: DeleteProjectByIdFunction
) : GraphQLMutationResolver {

    fun deleteProjectById(id: String): Boolean = withErrorHandling {
        when (deleteProjectById(UUID.fromString(id))) {
            ProjectNotFound -> false
            SuccessfullyDeleted -> true
        }
    }

}
