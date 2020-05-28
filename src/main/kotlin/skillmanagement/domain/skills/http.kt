package skillmanagement.domain.skills

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import java.util.UUID

@Relation(itemRelation = "skill", collectionRelation = "skills")
data class SkillResource(
    val id: UUID,
    val label: SkillLabel
) : RepresentationModel<SkillResource>()

fun Skill.toResource(): SkillResource = SkillResource(id, label)
    .apply { add(linkToCurrentMapping().slash("api/skills/${id}").withSelfRel()) }
