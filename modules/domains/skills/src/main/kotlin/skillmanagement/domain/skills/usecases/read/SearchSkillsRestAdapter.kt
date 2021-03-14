package skillmanagement.domain.skills.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.toSearchResource

@RestAdapter
@RequestMapping("/api/skills/_search")
internal class SearchSkillsRestAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) {

    @PostMapping
    fun post(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int,
        @RequestBody request: Request
    ): PagedModel<SkillResource> {
        val skills = getSkillsPage(SkillsMatchingQuery(PageIndex(page), PageSize(size), request.query))
        return skills.toSearchResource()
    }

    data class Request(
        val query: String
    )

}
