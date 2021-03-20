package skillmanagement.domain.projects.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toResource

@RestAdapter
@RequestMapping("/api/projects")
internal class GetProjectsPageRestAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) {

    @GetMapping
    fun get(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?
    ): PagedModel<ProjectResource> {
        val projects = getProjectsPage(query(page, size))
        return projects.toResource()
    }

    private fun query(page: Int?, size: Int?) =
        AllProjectsQuery(pageIndex = PageIndex.of(page), pageSize = PageSize.of(size))

}
