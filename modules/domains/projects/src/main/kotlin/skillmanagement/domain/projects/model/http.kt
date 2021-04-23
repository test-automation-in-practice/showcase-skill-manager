@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.projects.model

import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.common.http.toMetaData
import skillmanagement.common.model.Page

@Relation(itemRelation = "project", collectionRelation = "projects")
internal data class ProjectRepresentation(
    val id: ProjectId,
    val label: ProjectLabel,
    val description: ProjectDescription
) : RepresentationModel<ProjectRepresentation>()

internal fun ProjectEntity.toRepresentation() =
    ProjectRepresentation(
        id = id,
        label = label,
        description = description
    )

internal fun ProjectEntity.toResource() = toRepresentation()
    .apply {
        add(linkToProject(id).withSelfRel())
        add(linkToProject(id).withRel("delete"))
    }

internal fun Page<ProjectEntity>.toRepresentations() = withOtherContent(content.map(ProjectEntity::toRepresentation))

internal fun Page<ProjectEntity>.toResource(): PagedModel<ProjectRepresentation> =
    PagedModel.of(content.map(ProjectEntity::toResource), toMetaData())
        .apply {
            add(linkToProjects(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToProjects(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToProjects(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToProjects(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/projects$queryPart")
}

internal fun Page<ProjectEntity>.toSearchResource(): PagedModel<ProjectRepresentation> =
    PagedModel.of(content.map(ProjectEntity::toResource), toMetaData())
        .apply {
            add(linkToProjectsSearch(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToProjectsSearch(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToProjectsSearch(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToProjectsSearch(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "/_search?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/projects$queryPart")
}

internal fun linkToProject(id: ProjectId) =
    linkToCurrentMapping().slash("api/projects/$id")
