package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery

sealed class SkillsQuery

data class SkillsMatchingQuery(
    override val queryString: String,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, SkillsQuery()

data class AllSkillsQuery(
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedFindAllQuery, SkillsQuery()
