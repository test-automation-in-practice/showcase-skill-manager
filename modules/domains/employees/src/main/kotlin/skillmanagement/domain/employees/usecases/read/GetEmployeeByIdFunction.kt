package skillmanagement.domain.employees.usecases.read

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId

@BusinessFunction
class GetEmployeeByIdFunction internal constructor(
    private val getEmployeeFromDataStore: GetEmployeesFromDataStoreFunction
) {

    operator fun invoke(id: EmployeeId): EmployeeEntity? = getEmployeeFromDataStore(id)

}
