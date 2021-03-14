package skillmanagement.domain.skills.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import java.util.Collections

@GraphQLAdapter
internal class CreateSkillGraphQLAdapter(
    private val createSkill: CreateSkillFunction
) : GraphQLMutationResolver {

    fun createSkill(label: String, description: String?, tags: List<String>?): Skill = withErrorHandling {
        createSkill(
            label = SkillLabel(label),
            description = description?.let(::SkillDescription),
            tags = tags?.map(::Tag)?.toSortedSet() ?: Collections.emptySortedSet()
        )
    }

}
