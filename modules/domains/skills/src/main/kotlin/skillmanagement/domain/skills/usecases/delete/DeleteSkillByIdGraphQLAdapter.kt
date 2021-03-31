package skillmanagement.domain.skills.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import java.util.UUID

@GraphQLAdapter
internal class DeleteSkillByIdGraphQLAdapter(
    private val deleteSkillById: DeleteSkillByIdFunction
) : GraphQLMutationResolver {

    fun deleteSkillById(id: String): Boolean = deleteSkillById(UUID.fromString(id))

}
