package skillmanagement.domain.employees.usecases.delete

import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeDeletedEvent
import skillmanagement.domain.employees.usecases.read.GetEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteEmployeeByIdFunction internal constructor(
    private val getEmployeeById: GetEmployeeByIdFunction,
    private val deleteEmployeeFromDataStore: DeleteEmployeeFromDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    operator fun invoke(id: UUID): Boolean {
        val employee = getEmployeeById(id) ?: return false
        deleteEmployeeFromDataStore(id)
        publishEvent(EmployeeDeletedEvent(employee))
        return true
    }

}
