package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import java.util.UUID

sealed class EmployeesQuery

data class EmployeesWithSkill(
    val skillId: UUID,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, EmployeesQuery() {
    override val queryString: String = "_skillIds:$skillId"
}

data class EmployeesWhoWorkedOnProject(
    val projectId: UUID,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, EmployeesQuery() {
    override val queryString: String = "_projectIds:$projectId"
}

data class EmployeesMatchingQuery(
    override val queryString: String,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, EmployeesQuery()

data class AllEmployeesQuery(
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedFindAllQuery, EmployeesQuery()
