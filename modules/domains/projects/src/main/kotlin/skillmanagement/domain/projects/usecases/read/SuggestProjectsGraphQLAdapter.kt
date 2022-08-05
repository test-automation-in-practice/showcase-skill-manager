package skillmanagement.domain.projects.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectId

@GraphQLAdapter
internal class SuggestProjectsGraphQLAdapter(
    private val searchIndex: SearchIndex<ProjectEntity, ProjectId>
) {

    @QueryMapping
    fun suggestProjects(@Argument input: String, @Argument max: MaxSuggestions): List<Suggestion> =
        searchIndex.suggest(input = input, max = max)

}
