package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import skillmanagement.domain.employees.model.ProjectId
import skillmanagement.domain.employees.model.SkillId

sealed class EmployeesQuery

data class EmployeesWithSkill(
    val skillId: SkillId,
    override val pagination: Pagination = Pagination.DEFAULT
) : PagedStringQuery, EmployeesQuery() {
    override val queryString: String = "_skillIds:$skillId"
}

data class EmployeesWhoWorkedOnProject(
    val projectId: ProjectId,
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
