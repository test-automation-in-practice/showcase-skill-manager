package skillmanagement.domain.skills.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import java.util.UUID

@Component
internal class DeleteSkillByIdGraphQLMutation(
    private val deleteSkillById: DeleteSkillByIdFunction
) : GraphQLMutationResolver {

    fun deleteSkillById(id: String): Boolean = withErrorHandling {
        when (deleteSkillById(UUID.fromString(id))) {
            SkillNotFound -> false
            SuccessfullyDeleted -> true
        }
    }

}
