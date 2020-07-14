package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<Employee> {
        return findEmployeesInDataStore(query)
    }

}
