package skillmanagement.domain.employees.usecases.find

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex
import skillmanagement.domain.employees.usecases.get.GetEmployeeFromDataStore

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore,
    private val getEmployeeFromDataStore: GetEmployeeFromDataStore,
    private val searchIndex: EmployeeSearchIndex
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<Employee> =
        when (query) {
            is EmployeesWithSkill -> findEmployeesInDataStore(query)
            is EmployeesWhoWorkedOnProject -> findEmployeesInDataStore(query)
            is EmployeesMatchingQuery -> {
                val ids = searchIndex.query(query.queryString)
                getEmployeeFromDataStore(ids).values.toList()
            }
            NoOpQuery -> findEmployeesInDataStore()
        }

}
