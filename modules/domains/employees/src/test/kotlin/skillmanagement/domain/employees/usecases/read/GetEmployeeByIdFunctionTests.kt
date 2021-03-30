package skillmanagement.domain.employees.usecases.read

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetEmployeeByIdFunctionTests {

    private val id = employee_jane_doe.id

    private val getEmployeesFromDataStore: GetEmployeesFromDataStoreFunction = mockk()
    private val getEmployeeById = GetEmployeeByIdFunction(getEmployeesFromDataStore)

    @Test
    fun `returns NULL if nothing found with given ID`() {
        every { getEmployeesFromDataStore(id) } returns null
        getEmployeeById(id) shouldBe null
    }

    @Test
    fun `returns Employee if found by its ID`() {
        every { getEmployeesFromDataStore(id) } returns employee_jane_doe
        getEmployeeById(id) shouldBe employee_jane_doe
    }

}
