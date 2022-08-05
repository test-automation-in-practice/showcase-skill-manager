package skillmanagement.domain.skills.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.toRepresentation

@GraphQLAdapter
internal class GetSkillByIdGraphQLAdapter(
    private val delegate: GetSkillByIdFunction
) {

    @QueryMapping
    fun getSkillById(@Argument id: SkillId): SkillRepresentation? = delegate(id)?.toRepresentation()

}
