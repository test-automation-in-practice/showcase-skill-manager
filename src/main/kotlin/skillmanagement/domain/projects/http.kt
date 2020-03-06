package skillmanagement.domain.projects

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import java.util.UUID

@Relation(itemRelation = "project", collectionRelation = "projects")
data class ProjectResource(
    val id: UUID,
    val label: ProjectLabel,
    val description: ProjectDescription
) : RepresentationModel<ProjectResource>()

fun Project.toResource(): ProjectResource {
    val resource = ProjectResource(id, label, description)
    resource.add(linkToCurrentMapping().slash("api/projects/${id}").withSelfRel())
    return resource
}
