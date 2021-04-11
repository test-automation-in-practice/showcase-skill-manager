package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectId

@GraphQLAdapter
internal class SuggestProjectsGraphQLAdapter(
    private val searchIndex: SearchIndex<ProjectEntity, ProjectId>
) : GraphQLQueryResolver {

    fun suggestProjects(input: String, max: MaxSuggestions?): List<Suggestion> =
        searchIndex.suggest(input = input, max = max ?: MaxSuggestions.DEFAULT)

}
