package skillmanagement.domain.projects.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.ProjectRepresentation
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
    ): PagedModel<ProjectRepresentation> =
        getProjectsPage(query(page, size)).toResource()

    private fun query(page: Int?, size: Int?) =
        AllProjectsQuery(Pagination(PageIndex.of(page), PageSize.of(size)))

}
