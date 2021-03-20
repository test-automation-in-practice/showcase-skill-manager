package skillmanagement.domain.skills.usecases.read

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.Skill

@RestAdapter
@RequestMapping("/api/skills/_suggest")
internal class SuggestSkillsRestAdapter(
    private val searchIndex: SearchIndex<Skill>
) {

    @PostMapping
    fun post(
        @RequestParam(required = false) max: Int?,
        @RequestBody request: Request
    ): List<Suggestion> = searchIndex.suggest(
        input = request.input,
        max = max?.let(::MaxSuggestions) ?: MaxSuggestions.DEFAULT
    )

    data class Request(
        val input: String
    )

}
