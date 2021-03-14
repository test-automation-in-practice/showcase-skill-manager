package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import java.util.UUID

sealed class EmployeesQuery

data class EmployeesWithSkill(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT,
    val skillId: UUID
) : PagedStringQuery, EmployeesQuery() {
    override val queryString: String = "_skillIds:$skillId"
}

data class EmployeesWhoWorkedOnProject(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT,
    val projectId: UUID
) : PagedStringQuery, EmployeesQuery() {
    override val queryString: String = "_projectIds:$projectId"
}

data class EmployeesMatchingQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT,
    override val queryString: String
) : PagedStringQuery, EmployeesQuery()

data class AllEmployeesQuery(
    override val pageIndex: PageIndex = PageIndex.DEFAULT,
    override val pageSize: PageSize = PageSize.DEFAULT
) : PagedFindAllQuery, EmployeesQuery()
