package skillmanagement.domain.employees.get

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeRepository
import skillmanagement.domain.employees.toEmployee
import skillmanagement.domain.projects.get.GetProjectFromDataStore
import skillmanagement.domain.skills.get.GetSkillFromDataStore
import java.util.*

@TechnicalFunction
class GetEmployeeFromDataStore(
    private val repository: EmployeeRepository,
    private val getSkillFromDataStore: GetSkillFromDataStore,
    private val getProjectFromDataStore: GetProjectFromDataStore
) {

    operator fun invoke(id: UUID): Employee? = repository.findById(id)
        .map { document ->
            document.toEmployee(
                skillResolver = getSkillFromDataStore::invoke,
                projectResolver = getProjectFromDataStore::invoke
            )
        }
        .orElse(null)

}
