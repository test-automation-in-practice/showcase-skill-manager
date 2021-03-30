package skillmanagement.domain.employees.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.model.employee_creation_data_jane_doe
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class CreateEmployeeGraphQLAdapterTests {

    private val createEmployee: CreateEmployeeFunction = mockk()
    private val cut = CreateEmployeeGraphQLAdapter(createEmployee)

    @Test
    fun `delegates creation to business function`() {
        every { createEmployee(employee_creation_data_jane_doe) } returns employee_jane_doe
        assertThat(cut.createEmployee(employee_creation_data_jane_doe)).isEqualTo(employee_jane_doe)
    }

}
