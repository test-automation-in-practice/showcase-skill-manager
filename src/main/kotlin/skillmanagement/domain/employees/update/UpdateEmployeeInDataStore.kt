package skillmanagement.domain.employees.update

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeRepository
import skillmanagement.domain.employees.toDocument

@TechnicalFunction
class UpdateEmployeeInDataStore(
    private val repository: EmployeeRepository
) {

    operator fun invoke(employee: Employee) {
        require(repository.existsById(employee.id)) {
            "Employee [${employee.id}] does not yet exist and can therefor not be updated!"
        }
        repository.save(employee.toDocument())
    }

}
