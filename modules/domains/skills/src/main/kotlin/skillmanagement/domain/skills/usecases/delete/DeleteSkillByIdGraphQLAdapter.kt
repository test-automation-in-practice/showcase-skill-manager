package skillmanagement.domain.skills.usecases.delete

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillId

@GraphQLAdapter
internal class DeleteSkillByIdGraphQLAdapter(
    private val delegate: DeleteSkillByIdFunction
) {

    @MutationMapping
    fun deleteSkillById(@Argument id: SkillId): Boolean = delegate(id)

}
