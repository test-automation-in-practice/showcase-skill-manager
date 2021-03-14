package skillmanagement.domain.employees.usecases.read

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.Employee

@RestAdapter
@RequestMapping("/api/employees/_suggest")
internal class SuggestEmployeesRestAdapter(
    private val searchIndex: SearchIndex<Employee>
) {

    @PostMapping
    fun post(
        @RequestParam(defaultValue = "100") max: Int,
        @RequestBody request: Request
    ): List<Suggestion> = searchIndex.suggest(input = request.input, max = MaxSuggestions(max))

    data class Request(
        val input: String
    )

}