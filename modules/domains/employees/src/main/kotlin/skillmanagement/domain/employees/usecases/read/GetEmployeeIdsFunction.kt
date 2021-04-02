package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.model.Page
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeId

@BusinessFunction
class GetEmployeeIdsFunction internal constructor(
    private val searchIndex: SearchIndex<Employee, EmployeeId>
) {

    operator fun invoke(query: EmployeesQuery): Page<EmployeeId> =
        when (query) {
            is EmployeesWithSkill -> searchIndex.query(query)
            is EmployeesWhoWorkedOnProject -> searchIndex.query(query)
            is EmployeesMatchingQuery -> searchIndex.query(query)
            is AllEmployeesQuery -> searchIndex.findAll(query)
        }

}
