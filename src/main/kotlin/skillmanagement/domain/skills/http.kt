package skillmanagement.domain.skills

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import java.util.UUID

private const val RESOURCE_BASE = "api/skills"

@Relation(itemRelation = "skill", collectionRelation = "skills")
data class SkillResource(
    val id: UUID,
    val label: SkillLabel
) : RepresentationModel<SkillResource>()

fun Collection<Skill>.toResource(): CollectionModel<SkillResource> {
    val content = map(Skill::toResource)
    val selfLink = linkToSkills().withSelfRel()
    return CollectionModel.of(content, selfLink)
}

fun Skill.toResource() = SkillResource(
    id = id,
    label = label
).apply {
    add(linkToSkill(id).withSelfRel())
    add(linkToSkill(id).withRel("delete"))
}

fun linkToSkills() =
    linkToCurrentMapping().slash(RESOURCE_BASE)

fun linkToSkill(id: UUID) =
    linkToCurrentMapping().slash("$RESOURCE_BASE/$id")
