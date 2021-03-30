package skillmanagement.domain.employees.usecases.read

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.employees.model.Employee
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.uuid
import java.util.UUID

@UnitTest
@ResetMocksAfterEachTest
internal class GetEmployeeIdsFunctionTests {

    private val searchIndex: SearchIndex<Employee> = mockk()
    private val findEmployees = GetEmployeeIdsFunction(searchIndex)

    @Test
    fun `AllEmployeesQuery just returns all employee IDs from search index`() {
        val query = AllEmployeesQuery()
        val page:Page<UUID> = mockk()
        every { searchIndex.findAll(query) } returns page

        findEmployees(query) shouldBe page
    }

    @Test
    fun `EmployeesMatchingQuery gets IDs from search index`() {
        val query = EmployeesMatchingQuery(queryString = "jane")
        val page:Page<UUID> = mockk()
        every { searchIndex.query(query) } returns page

        findEmployees(query) shouldBe page
    }

    @Test
    fun `EmployeesWhoWorkedOnProject query gets IDs from search index`() {
        val query = EmployeesWhoWorkedOnProject(uuid())
        val page:Page<UUID> = mockk()
        every { searchIndex.query(query) } returns page

        findEmployees(query) shouldBe page
    }

    @Test
    fun `EmployeesWithSkill query gets IDs from search index`() {
        val query = EmployeesWithSkill(uuid())
        val page:Page<UUID> = mockk()
        every { searchIndex.query(query) } returns page

        findEmployees(query) shouldBe page
    }

}
