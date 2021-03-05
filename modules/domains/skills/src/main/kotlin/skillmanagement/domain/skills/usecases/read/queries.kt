package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery

sealed class SkillsQuery

data class SkillsMatchingQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT,
    override val queryString: String
) : PagedStringQuery, SkillsQuery()

data class AllSkillsQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT
) : PagedFindAllQuery, SkillsQuery()
