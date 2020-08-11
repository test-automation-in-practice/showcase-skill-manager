package skillmanagement.domain.employees.usecases.find

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex
import java.util.UUID

@BusinessFunction
class FindEmployeeIds(
    private val findEmployeeIdsInDataStore: FindEmployeeIdsInDataStore,
    private val searchIndex: EmployeeSearchIndex
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<UUID> =
        when (query) {
            is EmployeesWithSkill -> findEmployeeIdsInDataStore(query)
            is EmployeesWhoWorkedOnProject -> findEmployeeIdsInDataStore(query)
            is EmployeesMatchingQuery -> searchIndex.query(query.queryString)
            NoOpQuery -> findEmployeeIdsInDataStore()
        }

}
