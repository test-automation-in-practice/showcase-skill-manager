package skillmanagement.domain.employees.get

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import java.util.UUID

@BusinessFunction
class GetEmployeeById(
    private val getEmployeeFromDataStore: GetEmployeeFromDataStore,
    private val getEmployeeSkillsFromDataStore: GetEmployeeSkillsFromDataStore,
    private val getEmployeeProjectsFromDataStore: GetEmployeeProjectsFromDataStore
) {

    // TODO: which kinds of users should be able to access this information? The employee + their managers?
    operator fun invoke(id: UUID, includeSkills: Boolean = false, includeProjects: Boolean = false): Employee? {
        var employee = getEmployeeFromDataStore(id) ?: return null
        if (includeSkills) {
            employee = employee.copy(skills = getEmployeeSkillsFromDataStore(id))
        }
        if (includeProjects) {
            employee = employee.copy(projects = getEmployeeProjectsFromDataStore(id))
        }
        return employee
    }

}
