package skillmanagement.domain.employees.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.EmailAddress
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.TelephoneNumber
import skillmanagement.domain.employees.Title
import java.time.Clock

@BusinessFunction
class AddEmployee(
    private val idGenerator: IdGenerator,
    private val insertEmployeeIntoDataStore: InsertEmployeeIntoDataStore,
    private val clock: Clock
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(
        firstName: FirstName,
        lastName: LastName,
        title: Title,
        email: EmailAddress,
        telephone: TelephoneNumber
    ): Employee {
        val employee = Employee(
            id = idGenerator.generateId(),
            version = 1,
            firstName = firstName,
            lastName = lastName,
            title = title,
            email = email,
            telephone = telephone,
            skills = emptyList(),
            projects = emptyList(),
            lastUpdate = clock.instant()
        )
        insertEmployeeIntoDataStore(employee)
        return employee
    }

}
