package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import java.util.UUID

@BusinessFunction
class FindEmployeeIds(
    private val findEmployeeIdsInDataStore: FindEmployeeIdsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<UUID> {
        return findEmployeeIdsInDataStore(query)
    }

}
