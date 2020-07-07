package skillmanagement.domain.employees.find

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.employee_max_mustermann
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class FindEmployeesTests {

    val findEmployeesInDataStore: FindEmployeesInDataStore = mockk()
    val findEmployees = FindEmployees(findEmployeesInDataStore)

    @Test
    fun `returns empty list of no employees were found`() {
        every { findEmployeesInDataStore(any()) } returns emptyList()
        findEmployees() shouldBe emptyList()
    }

    @Test
    fun `returns whatever the underlying data store returns`() {
        val queryParameter = QueryParameter()
        every { findEmployeesInDataStore(queryParameter) } returns listOf(employee_max_mustermann)
        findEmployees(queryParameter) shouldBe listOf(employee_max_mustermann)
    }

}
