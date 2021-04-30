package skillmanagement.domain.employees.usecases.update

import arrow.core.Either
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.common.success
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent
import skillmanagement.domain.employees.usecases.read.GetEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotFound

@BusinessFunction
class UpdateEmployeeByIdFunction internal constructor(
    private val getEmployeeById: GetEmployeeByIdFunction,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(
        employeeId: EmployeeId,
        block: (Employee) -> Employee
    ): Either<EmployeeUpdateFailure, EmployeeEntity> {
        val currentEmployee = getEmployeeById(employeeId) ?: return failure(EmployeeNotFound)
        val modifiedEmployee = currentEmployee.update(block)

        if (currentEmployee == modifiedEmployee) return failure(EmployeeNotChanged(currentEmployee))

        val updatedEmployee = updateEmployeeInDataStore(modifiedEmployee)
        publishEvent(EmployeeUpdatedEvent(updatedEmployee))
        return success(updatedEmployee)
    }

}

sealed class EmployeeUpdateFailure {
    object EmployeeNotFound : EmployeeUpdateFailure()
    data class EmployeeNotChanged(val employee: EmployeeEntity) : EmployeeUpdateFailure()
}
