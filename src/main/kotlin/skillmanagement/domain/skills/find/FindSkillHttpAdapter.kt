package skillmanagement.domain.skills.find

import org.springframework.data.domain.Page
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.toResource
import javax.validation.constraints.Min

@HttpAdapter
@RequestMapping("/api/skills")
class FindSkillHttpAdapter(
    private val findSkills: FindSkills
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "100") @Min(0) size: Int
    ): PagedModel<SkillResource> {
        val skillPage = findSkills(page, size)

        val content = skillPage.content.map(Skill::toResource)
        val metadata = skillPage.toMetadata()
        val links = skillPage.toLinks()

        return PagedModel(content, metadata, links)
    }

    private fun Page<*>.toMetadata() =
        PagedModel.PageMetadata(size.toLong(), number.toLong(), totalElements, totalPages.toLong())

    private fun Page<*>.toLinks(): List<Link> {
        val controller = FindSkillHttpAdapter::class.java
        val links = mutableListOf(linkTo(methodOn(controller).get(number, size)).withSelfRel())
        if (!isFirst) {
            links += linkTo(methodOn(controller).get(0, size)).withRel("firstPage")
            links += linkTo(methodOn(controller).get(number - 1, size)).withRel("previousPage")
        }
        if (!isLast) {
            links += linkTo(methodOn(controller).get(number + 1, size)).withRel("nextPage")
            links += linkTo(methodOn(controller).get(totalPages - 1, size)).withRel("lastPage")
        }
        return links
    }

}
