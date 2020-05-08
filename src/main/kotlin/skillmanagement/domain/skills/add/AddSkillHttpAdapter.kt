package skillmanagement.domain.skills.add

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.toResource

@HttpAdapter
@RequestMapping("/api/skills")
class AddSkillHttpAdapter(
    private val addSkill: AddSkill
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: Request): SkillResource {
        val skill = addSkill(request.label)
        return skill.toResource()
    }

    data class Request(
        val label: SkillLabel
    )

}
