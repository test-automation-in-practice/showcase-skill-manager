package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.model.Page
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId

@BusinessFunction
class GetEmployeesPageFunction internal constructor(
    private val getEmployeeFromDataStore: GetEmployeesFromDataStoreFunction,
    private val searchIndex: SearchIndex<EmployeeEntity, EmployeeId>
) {

    operator fun invoke(query: EmployeesQuery): Page<EmployeeEntity> {
        val page = when (query) {
            is EmployeesWithSkill -> searchIndex.query(query)
            is EmployeesWhoWorkedOnProject -> searchIndex.query(query)
            is EmployeesMatchingQuery -> searchIndex.query(query)
            is AllEmployeesQuery -> searchIndex.findAll(query)
        }
        val employeesMap = getEmployeeFromDataStore(page.content)
        val employees = page.content.mapNotNull { employeesMap[it] }
        return page.withOtherContent(employees)
    }

}
