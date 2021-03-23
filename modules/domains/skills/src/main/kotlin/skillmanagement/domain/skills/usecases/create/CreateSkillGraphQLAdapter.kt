package skillmanagement.domain.skills.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import java.util.Collections.emptySortedSet

@GraphQLAdapter
internal class CreateSkillGraphQLAdapter(
    private val createSkill: CreateSkillFunction
) : GraphQLMutationResolver {

    fun createSkill(input: SkillInput): Skill =
        createSkill(
            label = input.label,
            description = input.description,
            tags = input.tags?.toSortedSet() ?: emptySortedSet()
        )

    data class SkillInput(
        val label: SkillLabel,
        val description: SkillDescription?,
        val tags: Set<Tag>?
    )

}
