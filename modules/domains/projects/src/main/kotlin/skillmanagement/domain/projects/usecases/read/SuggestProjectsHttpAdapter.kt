package skillmanagement.domain.projects.usecases.read

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.Suggestion
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex

@HttpAdapter
@RequestMapping("/api/projects/_suggest")
class SuggestProjectsHttpAdapter(
    private val searchIndex: ProjectSearchIndex
) {

    @PostMapping
    fun post(
        @RequestParam(defaultValue = "100") size: Int,
        @RequestBody request: Request
    ): List<Suggestion> =
        searchIndex.suggestExisting(input = request.input, size = size)

    data class Request(
        val input: String
    )

}
