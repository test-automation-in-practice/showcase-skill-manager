package skillmanagement.domain.projects.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.search.PageIndex
import skillmanagement.common.search.PageSize
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toAllResource

@HttpAdapter
@RequestMapping("/api/projects")
class GetProjectsHttpAdapter(
    private val getProjects: GetProjectsFunction
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): PagedModel<ProjectResource> {
        val projects = getProjects(AllProjectsQuery(PageIndex(page), PageSize(size)))
        return projects.toAllResource()
    }

}
