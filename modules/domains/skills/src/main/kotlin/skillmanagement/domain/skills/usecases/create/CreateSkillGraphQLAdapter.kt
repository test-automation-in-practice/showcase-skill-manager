package skillmanagement.domain.skills.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.graphql.withErrorHandling
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

    fun createSkill(input: SkillInput): Skill = withErrorHandling {
        createSkill(
            label = SkillLabel(input.label),
            description = input.description?.let(::SkillDescription),
            tags = input.tags?.map(::Tag)?.toSortedSet() ?: emptySortedSet()
        )
    }

    data class SkillInput(
        val label: String,
        val description: String?,
        val tags: Set<String>?
    )

}
