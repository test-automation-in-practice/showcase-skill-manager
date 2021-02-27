package skillmanagement.domain.skills.usecases.create

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.toResource
import java.util.Collections.emptySortedSet
import java.util.SortedSet

/**
 * Exposes [CreateSkillFunction] over HTTP.
 */
@HttpAdapter
@RequestMapping("/api/skills")
class CreateSkillHttpAdapter(
    private val createSkill: CreateSkillFunction
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: Request): SkillResource {
        val skill = createSkill(
            label = request.label,
            description = request.description,
            tags = request.tags
        )
        return skill.toResource()
    }

    data class Request(
        val label: SkillLabel,
        val description: SkillDescription? = null,
        val tags: SortedSet<Tag> = emptySortedSet()
    )

}
