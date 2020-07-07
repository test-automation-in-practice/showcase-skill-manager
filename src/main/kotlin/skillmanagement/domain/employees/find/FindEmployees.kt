package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import java.util.UUID

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindEmployeeQuery = NoOpQuery): List<Employee> {
        return findEmployeesInDataStore(query)
    }

}

sealed class FindEmployeeQuery
data class EmployeesWithSkill(val skillId: UUID) : FindEmployeeQuery()
data class EmployeesWhoWorkedOnProject(val projectId: UUID) : FindEmployeeQuery()
object NoOpQuery : FindEmployeeQuery()
