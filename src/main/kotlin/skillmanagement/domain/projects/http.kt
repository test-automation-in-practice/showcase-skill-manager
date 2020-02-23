package skillmanagement.domain.projects

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping

data class ProjectResource(
    val label: ProjectLabel,
    val description: ProjectDescription
) : RepresentationModel<ProjectResource>()

fun Project.toResource(): ProjectResource {
    val resource = ProjectResource(label, description)
    resource.add(linkToCurrentMapping().slash("api/projects/${id}").withSelfRel())
    return resource
}
