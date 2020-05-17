package skillmanagement.domain.skills.find

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.toResource

@HttpAdapter
@RequestMapping("/api/skills")
class FindSkillHttpAdapter(
    private val findSkills: FindSkills
) {

    @GetMapping
    fun get(): CollectionModel<SkillResource> {
        val skills = findSkills()

        val content = skills.map(Skill::toResource)
        val selfLink = linkToCurrentMapping().slash("api/skills").withSelfRel()

        return CollectionModel.of(content, selfLink)
    }

}
