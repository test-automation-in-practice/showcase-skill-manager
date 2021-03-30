package skillmanagement.domain.employees.usecases.update

import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent
import skillmanagement.domain.employees.usecases.read.GetEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class UpdateEmployeeByIdFunction internal constructor(
    private val getEmployeeById: GetEmployeeByIdFunction,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(employeeId: UUID, block: (Employee) -> Employee): UpdateEmployeeByIdResult {
        val currentEmployee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val modifiedEmployee = block(currentEmployee)

        if (currentEmployee == modifiedEmployee) return EmployeeNotChanged(currentEmployee)
        assertNoInvalidModifications(currentEmployee, modifiedEmployee)

        val updatedEmployee = updateEmployeeInDataStore(modifiedEmployee)
        publishEvent(EmployeeUpdatedEvent(updatedEmployee))
        return SuccessfullyUpdated(updatedEmployee)
    }

    private fun assertNoInvalidModifications(currentEmployee: Employee, modifiedEmployee: Employee) {
        check(currentEmployee.id == modifiedEmployee.id) { "ID must not be changed!" }
        check(currentEmployee.version == modifiedEmployee.version) { "Version must not be changed!" }
        check(currentEmployee.lastUpdate == modifiedEmployee.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class UpdateEmployeeByIdResult {
    object EmployeeNotFound : UpdateEmployeeByIdResult()
    data class EmployeeNotChanged(val employee: Employee) : UpdateEmployeeByIdResult()
    data class SuccessfullyUpdated(val employee: Employee) : UpdateEmployeeByIdResult()
}
