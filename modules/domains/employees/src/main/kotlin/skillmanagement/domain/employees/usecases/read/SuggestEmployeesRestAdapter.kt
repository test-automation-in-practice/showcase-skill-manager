package skillmanagement.domain.employees.usecases.read

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId

@RestAdapter
@RequestMapping("/api/employees/_suggest")
internal class SuggestEmployeesRestAdapter(
    private val searchIndex: SearchIndex<EmployeeEntity, EmployeeId>
) {

    @PostMapping
    fun post(
        @RequestParam(required = false) max: Int?,
        @RequestBody request: Request
    ): List<Suggestion> = searchIndex.suggest(input = request.input, max = MaxSuggestions.of(max))

    data class Request(
        val input: String
    )

}
