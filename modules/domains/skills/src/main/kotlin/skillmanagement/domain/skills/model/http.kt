@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.skills.model

import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.common.http.toMetaData
import skillmanagement.common.model.Page
import java.util.SortedSet

@Relation(itemRelation = "skill", collectionRelation = "skills")
internal data class SkillRepresentation(
    val id: SkillId,
    val label: SkillLabel,
    val description: SkillDescription?,
    val tags: SortedSet<Tag>
) : RepresentationModel<SkillRepresentation>()

internal fun SkillEntity.toRepresentation() =
    SkillRepresentation(
        id = id,
        label = data.label,
        description = data.description,
        tags = data.tags
    )

internal fun SkillEntity.toResource() = toRepresentation()
    .apply {
        add(linkToSkill(id).withSelfRel())
        add(linkToSkill(id).withRel("delete"))
    }

internal fun Page<SkillEntity>.toRepresentations() = withOtherContent(content.map(SkillEntity::toRepresentation))

internal fun Page<SkillEntity>.toResource(): PagedModel<SkillRepresentation> =
    PagedModel.of(content.map(SkillEntity::toResource), toMetaData())
        .apply {
            add(linkToSkills(pageIndex, pageSize).withSelfRel())
            if (hasPrevious) add(linkToSkills(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext) add(linkToSkills(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToSkills(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/skills$queryPart")
}

internal fun Page<SkillEntity>.toSearchResource(): PagedModel<SkillRepresentation> =
    PagedModel.of(content.map(SkillEntity::toResource), toMetaData())
        .apply {
            add(linkToSkillsSearch(pageIndex, pageSize).withSelfRel())
            if (hasPrevious) add(linkToSkillsSearch(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext) add(linkToSkillsSearch(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToSkillsSearch(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "/_search?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/skills$queryPart")
}

internal fun linkToSkill(id: SkillId) =
    linkToCurrentMapping().slash("api/skills/$id")
