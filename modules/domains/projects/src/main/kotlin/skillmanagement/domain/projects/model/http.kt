@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.projects.model

import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.common.http.toMetaData
import skillmanagement.common.model.Page
import java.util.UUID

@Relation(itemRelation = "project", collectionRelation = "projects")
internal data class ProjectResource(
    val id: UUID,
    val label: ProjectLabel,
    val description: ProjectDescription
) : RepresentationModel<ProjectResource>()

internal fun Project.toResource() = ProjectResource(
    id = id,
    label = label,
    description = description
).apply {
    add(linkToProject(id).withSelfRel())
    add(linkToProject(id).withRel("delete"))
}

internal fun Page<Project>.toAllResource(): PagedModel<ProjectResource> =
    PagedModel.of(content.map(Project::toResource), toMetaData())
        .apply {
            add(linkToProjects(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToProjects(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToProjects(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToProjects(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/projects$queryPart")
}

internal fun Page<Project>.toSearchResource(): PagedModel<ProjectResource> =
    PagedModel.of(content.map(Project::toResource), toMetaData())
        .apply {
            add(linkToProjectsSearch(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToProjectsSearch(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToProjectsSearch(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToProjectsSearch(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "/_search?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/projects$queryPart")
}

internal fun linkToProject(id: UUID) =
    linkToCurrentMapping().slash("api/projects/$id")
