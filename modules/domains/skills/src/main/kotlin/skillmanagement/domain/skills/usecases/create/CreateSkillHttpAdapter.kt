package skillmanagement.domain.skills.usecases.create

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.skills.model.Skill
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
internal class CreateSkillHttpAdapter(
    private val createSkill: CreateSkillFunction
) {

    @PostMapping
    fun post(@RequestBody request: Request): ResponseEntity<SkillResource> {
        val skill = createSkill(
            label = request.label,
            description = request.description,
            tags = request.tags
        )
        val location = locationOf(skill)
        val resource = skill.toResource()
        return created(location).body(resource)
    }

    private fun locationOf(skill: Skill) = fromCurrentRequest().path("/${skill.id}").build().toUri()

    data class Request(
        val label: SkillLabel,
        val description: SkillDescription? = null,
        val tags: SortedSet<Tag> = emptySortedSet()
    )

}
