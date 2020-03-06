package skillmanagement.domain.employees.delete

import skillmanagement.domain.BusinessFunction
import java.util.UUID

@BusinessFunction
class DeleteEmployeeById(
    private val deleteEmployeeFromDataStore: DeleteEmployeeFromDataStore
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(id: UUID) {
        deleteEmployeeFromDataStore(id)
    }

}
