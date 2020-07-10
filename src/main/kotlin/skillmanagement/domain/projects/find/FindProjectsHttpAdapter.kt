package skillmanagement.domain.projects.find

import org.springframework.hateoas.CollectionModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.projects.ProjectResource
import skillmanagement.domain.projects.toResource

@HttpAdapter
@RequestMapping("/api/projects")
class FindProjectsHttpAdapter(
    private val findProjects: FindProjects
) {

    @GetMapping
    fun get(@RequestParam query: String?): CollectionModel<ProjectResource> {
        val projects = when (query?.trim()) {
            null, "" -> findProjects()
            else -> findProjects(ProjectsWithLabelLike(query))
        }
        return projects.toResource()
    }

}
