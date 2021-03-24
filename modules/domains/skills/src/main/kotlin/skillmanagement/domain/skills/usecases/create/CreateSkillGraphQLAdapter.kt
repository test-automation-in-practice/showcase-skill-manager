package skillmanagement.domain.skills.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillCreationData

@GraphQLAdapter
internal class CreateSkillGraphQLAdapter(
    private val createSkillFunction: CreateSkillFunction
) : GraphQLMutationResolver {

    fun createSkill(input: SkillCreationData): Skill = createSkillFunction(input)

}
