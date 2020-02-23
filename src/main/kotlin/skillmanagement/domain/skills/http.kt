package skillmanagement.domain.skills

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping

data class SkillResource(
    val label: SkillLabel
) : RepresentationModel<SkillResource>()

fun Skill.toResource(): SkillResource {
    val resource = SkillResource(label)
    resource.add(linkToCurrentMapping().slash("api/skills/${id}").withSelfRel())
    return resource
}
