package skillmanagement.domain.employees.find

import java.util.UUID

sealed class FindEmployeeQuery
data class EmployeesWithSkill(val skillId: UUID) : FindEmployeeQuery()
data class EmployeesWhoWorkedOnProject(val projectId: UUID) : FindEmployeeQuery()
object NoOpQuery : FindEmployeeQuery()
