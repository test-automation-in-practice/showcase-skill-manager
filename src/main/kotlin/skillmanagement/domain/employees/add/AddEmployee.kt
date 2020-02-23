package skillmanagement.domain.employees.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName

@BusinessFunction
class AddEmployee(
    private val idGenerator: IdGenerator,
    private val insertEmployeeIntoDataStore: InsertEmployeeIntoDataStore
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(firstName: FirstName, lastName: LastName): Employee {
        val employee = Employee(
            id = idGenerator.generateId(),
            firstName = firstName,
            lastName = lastName,
            skills = emptyMap(),
            projects = emptyList()
        )
        insertEmployeeIntoDataStore(employee)
        return employee
    }

}
