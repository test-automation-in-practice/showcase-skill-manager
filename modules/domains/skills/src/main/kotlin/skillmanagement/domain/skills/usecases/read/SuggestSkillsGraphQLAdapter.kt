package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill

@GraphQLAdapter
internal class SuggestSkillsGraphQLAdapter(
    private val searchIndex: SearchIndex<Skill>
) : GraphQLQueryResolver {

    fun suggestSkills(input: String, max: Int?): List<Suggestion> = withErrorHandling {
        searchIndex.suggest(input = input, max = MaxSuggestions.of(max))
    }

}
