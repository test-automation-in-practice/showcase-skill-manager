package skillmanagement.domain.employees.usecases.delete

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.employees.model.EmployeeDeletedEvent
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.usecases.read.GetEmployeeByIdFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteEmployeeByIdFunctionTests {

    private val getEmployeeById: GetEmployeeByIdFunction = mockk()
    private val deleteEmployeeFromDataStore: DeleteEmployeeFromDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val deleteEmployeeById =
        DeleteEmployeeByIdFunction(getEmployeeById, deleteEmployeeFromDataStore, publishEvent)

    @Test
    fun `given employee with ID does not exist when deleting by ID then the result will be EmployeeNotFound`() {
        every { getEmployeeById(employee_jane_doe.id) } returns null

        deleteEmployeeById(employee_jane_doe.id) shouldBe DeleteEmployeeByIdResult.EmployeeNotFound

        verify { deleteEmployeeFromDataStore wasNot called }
        verify { publishEvent wasNot called }
    }

    @Test
    fun `given employee with ID exists when deleting by ID then the result will be SuccessfullyDeleted`() {
        every { getEmployeeById(employee_jane_doe.id) } returns employee_jane_doe

        deleteEmployeeById(employee_jane_doe.id) shouldBe DeleteEmployeeByIdResult.SuccessfullyDeleted

        verify { deleteEmployeeFromDataStore(employee_jane_doe.id) }
        verify { publishEvent(EmployeeDeletedEvent(employee_jane_doe)) }
    }

}
