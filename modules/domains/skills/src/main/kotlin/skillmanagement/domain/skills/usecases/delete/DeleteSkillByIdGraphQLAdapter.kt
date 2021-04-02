package skillmanagement.domain.skills.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.skillId

@GraphQLAdapter
internal class DeleteSkillByIdGraphQLAdapter(
    private val deleteSkillById: DeleteSkillByIdFunction
) : GraphQLMutationResolver {

    fun deleteSkillById(id: String): Boolean = deleteSkillById(skillId(id))

}
