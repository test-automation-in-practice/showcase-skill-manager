package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee

@BusinessFunction
class FindEmployees(
    private val findEmployeeInDataStore: FindEmployeeInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(): List<Employee> {
        return findEmployeeInDataStore()
    }

}
