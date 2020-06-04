package skillmanagement.domain.employees.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import java.util.UUID

@BusinessFunction
class FindEmployees(
    private val findEmployeesInDataStore: FindEmployeesInDataStore,
    private val findEmployeeSkillsInDataStore: FindEmployeeSkillsInDataStore,
    private val findEmployeeProjectsInDataStore: FindEmployeeProjectsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(includeSkills: Boolean = false, includeProjects: Boolean = false): List<Employee> {
        var employees = findEmployeesInDataStore()
        if (employees.isEmpty()) {
            return emptyList()
        }

        val employeeIds = employees.map { it.id }
        if (includeSkills) {
            val skills = findEmployeeSkillsInDataStore(employeeIds)
            employees = employees.map { employee -> employee.copy(skills = skills of employee) }
        }
        if (includeProjects) {
            val projects = findEmployeeProjectsInDataStore(employeeIds)
            employees = employees.map { employee -> employee.copy(projects = projects of employee) }
        }
        return employees
    }

    private infix fun <T> Map<UUID, List<T>>.of(employee: Employee) = get(employee.id) ?: emptyList()

}
