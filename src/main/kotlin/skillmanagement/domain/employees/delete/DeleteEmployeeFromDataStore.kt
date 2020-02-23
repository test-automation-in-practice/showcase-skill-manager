package skillmanagement.domain.employees.delete

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.EmployeeRepository
import java.util.*

@TechnicalFunction
class DeleteEmployeeFromDataStore(
    private val repository: EmployeeRepository
) {

    operator fun invoke(id: UUID) {
        repository.deleteById(id)
    }

}
