package skillmanagement.domain.projects.find

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectResource
import skillmanagement.domain.projects.toResource

@HttpAdapter
@RequestMapping("/api/projects")
class FindProjectsHttpAdapter(
    private val findProjects: FindProjects
) {

    @GetMapping
    fun get(): CollectionModel<ProjectResource> {
        val projects = findProjects()

        val content = projects.map(Project::toResource)
        val selfLink = linkToCurrentMapping().slash("api/projects").withSelfRel()

        return CollectionModel.of(content, selfLink)
    }

}
