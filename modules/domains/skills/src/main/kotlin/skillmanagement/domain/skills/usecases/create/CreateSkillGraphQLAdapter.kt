package skillmanagement.domain.skills.usecases.create

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.toRepresentation

@GraphQLAdapter
internal class CreateSkillGraphQLAdapter(
    private val delegate: CreateSkillFunction
) {

    @MutationMapping
    fun createSkill(
        @Argument label: SkillLabel,
        @Argument description: SkillDescription?,
        @Argument tags: List<Tag>
    ): SkillRepresentation {
        val data = SkillCreationData(
            label = label,
            description = description,
            tags = tags.toSortedSet()
        )
        return delegate(data).toRepresentation()
    }

}
