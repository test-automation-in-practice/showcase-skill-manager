package skillmanagement.domain.skills.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.toAllResource

@RestAdapter
@RequestMapping("/api/skills")
internal class GetSkillsPageRestAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): PagedModel<SkillResource> {
        val skills = getSkillsPage(AllSkillsQuery(PageIndex(page), PageSize(size)))
        return skills.toAllResource()
    }

}
