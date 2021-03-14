package skillmanagement.domain.projects.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toAllResource

@RestAdapter
@RequestMapping("/api/projects")
internal class GetProjectsPageRestAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): PagedModel<ProjectResource> {
        val projects = getProjectsPage(AllProjectsQuery(PageIndex(page), PageSize(size)))
        return projects.toAllResource()
    }

}
