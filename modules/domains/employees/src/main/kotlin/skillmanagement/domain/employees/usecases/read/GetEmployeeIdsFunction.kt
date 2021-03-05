package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.searchindices.Page
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex
import java.util.UUID

@BusinessFunction
class GetEmployeeIdsFunction internal constructor(
    private val searchIndex: EmployeeSearchIndex
) {

    operator fun invoke(query: EmployeesQuery): Page<UUID> =
        when (query) {
            is EmployeesWithSkill -> searchIndex.query(query)
            is EmployeesWhoWorkedOnProject -> searchIndex.query(query)
            is EmployeesMatchingQuery -> searchIndex.query(query)
            is AllEmployeesQuery -> searchIndex.findAll(query)
        }

}
