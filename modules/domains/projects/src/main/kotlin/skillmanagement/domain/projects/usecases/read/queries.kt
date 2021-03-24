package skillmanagement.domain.projects.usecases.read

import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery

sealed class ProjectsQuery

data class ProjectsMatchingQuery(
    override val queryString: String,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, ProjectsQuery()

data class AllProjectsQuery(
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedFindAllQuery, ProjectsQuery()
