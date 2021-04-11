package skillmanagement.domain.skills.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.toRepresentation

@GraphQLAdapter
internal class CreateSkillGraphQLAdapter(
    private val createSkillFunction: CreateSkillFunction
) : GraphQLMutationResolver {

    fun createSkill(input: SkillCreationData): SkillRepresentation = createSkillFunction(input).toRepresentation()

}
