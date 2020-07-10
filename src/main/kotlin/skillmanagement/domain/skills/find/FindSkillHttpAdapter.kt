package skillmanagement.domain.skills.find

import org.springframework.hateoas.CollectionModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.toResource

@HttpAdapter
@RequestMapping("/api/skills")
class FindSkillHttpAdapter(
    private val findSkills: FindSkills
) {

    @GetMapping
    fun get(@RequestParam query: String?): CollectionModel<SkillResource> {
        val skills = when (query?.trim()) {
            null, "" -> findSkills()
            else -> findSkills(SkillsWithLabelLike(query))
        }
        return skills.toResource()
    }

}
