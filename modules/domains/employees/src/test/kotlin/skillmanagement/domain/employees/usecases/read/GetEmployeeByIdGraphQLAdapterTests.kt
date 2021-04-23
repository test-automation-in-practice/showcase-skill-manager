package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_representation_jane_doe
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetEmployeeByIdGraphQLAdapterTests {

    private val getEmployeeById: GetEmployeeByIdFunction = mockk()
    private val cut = GetEmployeeByIdGraphQLAdapter(getEmployeeById)

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getEmployeeById(employee_jane_doe.id) } returns employee_jane_doe
        assertThat(tryToGetEmployee()).isEqualTo(employee_representation_jane_doe)
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getEmployeeById(employee_jane_doe.id) } returns null
        assertThat(tryToGetEmployee()).isNull()
    }

    private fun tryToGetEmployee(id: String = "9e1ff73e-0f66-4b86-8548-040d4016bfc9") = cut.getEmployeeById(id)

}
