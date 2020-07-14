package skillmanagement.domain.employees.update

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.update.UpdateEmplyeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.update.UpdateEmplyeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class UpdateEmployeeById(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Who can change Employees?
    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(employeeId: UUID, block: (Employee) -> Employee): UpdateEmplyeeByIdResult {
        val currentEmployee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val modifiedEmployee = block(currentEmployee)

        assertNoInvalidModifications(currentEmployee, modifiedEmployee)

        val updatedEmployee = updateEmployeeInDataStore(modifiedEmployee)
        return SuccessfullyUpdated(updatedEmployee)
    }

    private fun assertNoInvalidModifications(currentEmployee: Employee, modifiedEmployee: Employee) {
        check(currentEmployee.id == modifiedEmployee.id) { "ID must not be changed!" }
        check(currentEmployee.version == modifiedEmployee.version) { "Version must not be changed!" }
        check(currentEmployee.lastUpdate == modifiedEmployee.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class UpdateEmplyeeByIdResult {
    object EmployeeNotFound : UpdateEmplyeeByIdResult()
    data class SuccessfullyUpdated(val employee: Employee) : UpdateEmplyeeByIdResult()
}
