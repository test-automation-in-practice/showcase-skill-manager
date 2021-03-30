package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.Employee

@GraphQLAdapter
internal class SuggestEmployeesGraphQLAdapter(
    private val searchIndex: SearchIndex<Employee>
) : GraphQLQueryResolver {

    fun suggestEmployees(input: String, max: MaxSuggestions?): List<Suggestion> =
        searchIndex.suggest(input = input, max = max ?: MaxSuggestions.DEFAULT)

}
