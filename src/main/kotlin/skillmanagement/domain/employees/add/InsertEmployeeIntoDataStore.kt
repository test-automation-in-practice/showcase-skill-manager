package skillmanagement.domain.employees.add

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeRepository
import skillmanagement.domain.employees.toDocument

@TechnicalFunction
class InsertEmployeeIntoDataStore(
    private val repository: EmployeeRepository
) {

    operator fun invoke(employee: Employee) {
        repository.insert(employee.toDocument())
    }

}
