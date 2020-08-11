package skillmanagement.domain.projects.usecases.find

import org.springframework.hateoas.CollectionModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toResource

@HttpAdapter
@RequestMapping("/api/projects")
class FindProjectsHttpAdapter(
    private val findProjects: FindProjects
) {

    @GetMapping
    fun get(@RequestParam query: String?): CollectionModel<ProjectResource> {
        val projects = when (query?.trim()) {
            null, "" -> findProjects()
            else -> findProjects(ProjectsMatchingQuery(query))
        }
        return projects.toResource()
    }

}
