package skillmanagement.domain.employees.usecases.create

import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeAddedEvent
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import java.time.Clock

@BusinessFunction
class CreateEmployeeFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val insertEmployeeIntoDataStore: InsertEmployeeIntoDataStoreFunction,
    private val publishEvent: PublishEventFunction,
    private val clock: Clock
) {

    operator fun invoke(data: EmployeeCreationData): EmployeeEntity {
        val employee = data.toEmployee()
        insertEmployeeIntoDataStore(employee)
        publishEvent(EmployeeAddedEvent(employee))
        return employee
    }

    private fun EmployeeCreationData.toEmployee(): EmployeeEntity {
        val now = clock.instant()
        return EmployeeEntity(
            id = EmployeeId(idGenerator.generateId()),
            version = 1,
            data = Employee(
                firstName = firstName,
                lastName = lastName,
                title = title,
                email = email,
                telephone = telephone
            ),
            created = now,
            lastUpdate = now
        )
    }

}
