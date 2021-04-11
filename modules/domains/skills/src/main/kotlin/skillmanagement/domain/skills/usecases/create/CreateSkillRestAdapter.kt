package skillmanagement.domain.skills.usecases.create

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.toResource

/**
 * Exposes [CreateSkillFunction] over HTTP.
 */
@RestAdapter
@RequestMapping("/api/skills")
internal class CreateSkillRestAdapter(
    private val createSkill: CreateSkillFunction
) {

    @PostMapping
    fun post(@RequestBody request: SkillCreationData): ResponseEntity<SkillResource> {
        val skill = createSkill(request)
        val location = locationOf(skill)
        val resource = skill.toResource()
        return created(location).body(resource)
    }

    private fun locationOf(skill: SkillEntity) = fromCurrentRequest().path("/${skill.id}").build().toUri()

}
