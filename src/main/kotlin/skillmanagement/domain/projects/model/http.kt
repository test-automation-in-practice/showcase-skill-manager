package skillmanagement.domain.projects.model

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import java.util.UUID

private const val RESOURCE_BASE = "api/projects"

@Relation(itemRelation = "project", collectionRelation = "projects")
data class ProjectResource(
    val id: UUID,
    val label: ProjectLabel,
    val description: ProjectDescription
) : RepresentationModel<ProjectResource>()

fun Collection<Project>.toResource(): CollectionModel<ProjectResource> {
    val content = map(Project::toResource)
    val selfLink = linkToProjects().withSelfRel()
    return CollectionModel.of(content, selfLink)
}

fun Project.toResource() = ProjectResource(
    id = id,
    label = label,
    description = description
).apply {
    add(linkToProject(id).withSelfRel())
    add(linkToProject(id).withRel("delete"))
}

fun linkToProjects() =
    linkToCurrentMapping().slash(RESOURCE_BASE)

fun linkToProject(id: UUID) =
    linkToCurrentMapping().slash("$RESOURCE_BASE/${id}")
