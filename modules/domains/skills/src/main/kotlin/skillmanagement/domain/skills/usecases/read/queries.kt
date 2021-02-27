package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.search.PageIndex
import skillmanagement.common.search.PageSize
import skillmanagement.common.search.PagedFindAllQuery
import skillmanagement.common.search.PagedStringQuery

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
