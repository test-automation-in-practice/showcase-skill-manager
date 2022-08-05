package skillmanagement.domain.skills.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId

@GraphQLAdapter
internal class SuggestSkillsGraphQLAdapter(
    private val searchIndex: SearchIndex<SkillEntity, SkillId>
) {

    @QueryMapping
    fun suggestSkills(@Argument input: String, @Argument max: MaxSuggestions): List<Suggestion> =
        searchIndex.suggest(input = input, max = max)

}
