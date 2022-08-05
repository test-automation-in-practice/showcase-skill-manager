package skillmanagement.domain.employees.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId

@GraphQLAdapter
internal class SuggestEmployeesGraphQLAdapter(
    private val searchIndex: SearchIndex<EmployeeEntity, EmployeeId>
) {

    @QueryMapping
    fun suggestEmployees(@Argument input: String, @Argument max: MaxSuggestions): List<Suggestion> =
        searchIndex.suggest(input = input, max = max)

}
