package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.skillId
import skillmanagement.domain.skills.model.toRepresentation

@GraphQLAdapter
internal class GetSkillByIdGraphQLAdapter(
    private val getSkillById: GetSkillByIdFunction
) : GraphQLQueryResolver {

    fun getSkillById(id: String): SkillRepresentation? = getSkillById(skillId(id))?.toRepresentation()

}
