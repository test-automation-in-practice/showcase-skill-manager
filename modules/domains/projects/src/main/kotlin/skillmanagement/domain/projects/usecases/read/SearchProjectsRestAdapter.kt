package skillmanagement.domain.projects.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toSearchResource

@RestAdapter
@RequestMapping("/api/projects/_search")
internal class SearchProjectsRestAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) {

    @PostMapping
    fun post(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?,
        @RequestBody request: Request
    ): PagedModel<ProjectResource> {
        val projects = getProjectsPage(query(request.query, page, size))
        return projects.toSearchResource()
    }

    private fun query(query: String, page: Int?, size: Int?) =
        ProjectsMatchingQuery(queryString = query, pageIndex = PageIndex.of(page), pageSize = PageSize.of(size))

    data class Request(
        val query: String
    )

}
