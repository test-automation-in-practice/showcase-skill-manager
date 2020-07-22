package skillmanagement.domain.skills.add

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.Tag
import skillmanagement.domain.skills.toResource
import java.util.Collections.emptySortedSet
import java.util.SortedSet

/**
 * Exposes [AddSkill] over HTTP.
 */
@HttpAdapter
@RequestMapping("/api/skills")
class AddSkillHttpAdapter(
    private val addSkill: AddSkill
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: Request): SkillResource {
        val skill = addSkill(request.label, request.tags)
        return skill.toResource()
    }

    data class Request(
        val label: SkillLabel,
        val tags: SortedSet<Tag> = emptySortedSet()
    )

}
