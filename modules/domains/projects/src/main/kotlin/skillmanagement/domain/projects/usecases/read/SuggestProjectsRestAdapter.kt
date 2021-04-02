package skillmanagement.domain.projects.usecases.read

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectId

@RestAdapter
@RequestMapping("/api/projects/_suggest")
internal class SuggestProjectsRestAdapter(
    private val searchIndex: SearchIndex<Project, ProjectId>
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
