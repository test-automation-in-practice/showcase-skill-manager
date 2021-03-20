package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project

@GraphQLAdapter
internal class SuggestProjectsGraphQLAdapter(
    private val searchIndex: SearchIndex<Project>
) : GraphQLQueryResolver {

    fun suggestProjects(input: String, max: Int?): List<Suggestion> = withErrorHandling {
        searchIndex.suggest(input = input, max = MaxSuggestions.of(max))
    }

}
