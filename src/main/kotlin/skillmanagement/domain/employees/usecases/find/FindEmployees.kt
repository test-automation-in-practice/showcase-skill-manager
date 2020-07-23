package skillmanagement.domain.employees.usecases.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.model.Employee

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<Employee> {
        return findEmployeesInDataStore(query)
    }

}
