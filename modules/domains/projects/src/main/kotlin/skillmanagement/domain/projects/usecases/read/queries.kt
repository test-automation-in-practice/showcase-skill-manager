package skillmanagement.domain.projects.usecases.read

import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery

sealed class ProjectsQuery

data class ProjectsMatchingQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT,
    override val queryString: String
) : PagedStringQuery, ProjectsQuery()

data class AllProjectsQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT
) : PagedFindAllQuery, ProjectsQuery()
