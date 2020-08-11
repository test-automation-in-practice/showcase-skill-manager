package skillmanagement.domain.employees.usecases.find

import java.util.UUID

sealed class FindEmployeeQuery
data class EmployeesWithSkill(val skillId: UUID) : FindEmployeeQuery()
data class EmployeesWhoWorkedOnProject(val projectId: UUID) : FindEmployeeQuery()
data class EmployeesMatchingQuery(val queryString: String) : FindEmployeeQuery()
object NoOpQuery : FindEmployeeQuery()
