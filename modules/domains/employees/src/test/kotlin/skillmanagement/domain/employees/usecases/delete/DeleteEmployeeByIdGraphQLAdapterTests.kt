package skillmanagement.domain.employees.usecases.delete

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeByIdResult.SuccessfullyDeleted
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteEmployeeByIdGraphQLAdapterTests {

    private val deleteEmployeeById: DeleteEmployeeByIdFunction = mockk()
    private val cut = DeleteEmployeeByIdGraphQLAdapter(deleteEmployeeById)

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteEmployeeById(employee_jane_doe.id) } returns SuccessfullyDeleted
        assertThat(tryToDeleteSkill()).isTrue()
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteEmployeeById(employee_jane_doe.id) } returns EmployeeNotFound
        assertThat(tryToDeleteSkill()).isFalse()
    }

    private fun tryToDeleteSkill(id: String = "9e1ff73e-0f66-4b86-8548-040d4016bfc9") = cut.deleteEmployeeById(id)
}
