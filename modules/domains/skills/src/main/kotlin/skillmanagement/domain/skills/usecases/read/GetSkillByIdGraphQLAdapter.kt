package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.skillId

@GraphQLAdapter
internal class GetSkillByIdGraphQLAdapter(
    private val getSkillById: GetSkillByIdFunction
) : GraphQLQueryResolver {

    fun getSkillById(id: String): SkillEntity? = getSkillById(skillId(id))

}
