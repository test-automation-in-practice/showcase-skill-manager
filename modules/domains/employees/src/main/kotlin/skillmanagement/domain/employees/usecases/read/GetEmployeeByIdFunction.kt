package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import java.util.UUID

@BusinessFunction
class GetEmployeeByIdFunction(
    private val getEmployeeFromDataStore: GetEmployeesFromDataStoreFunction
) {

    operator fun invoke(id: UUID): Employee? {
        return getEmployeeFromDataStore(id)
    }

}
