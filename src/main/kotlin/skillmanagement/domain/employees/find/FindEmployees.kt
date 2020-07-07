package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import java.util.UUID

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(parameter: QueryParameter = QueryParameter()): List<Employee> {
        return findEmployeesInDataStore(parameter)
    }

}

data class QueryParameter(
    val skillId: UUID? = null,
    val projectId: UUID? = null
)
