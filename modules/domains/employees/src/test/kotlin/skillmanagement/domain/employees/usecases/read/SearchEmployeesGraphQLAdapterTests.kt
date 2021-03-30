package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.domain.employees.model.Employee
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SearchEmployeesGraphQLAdapterTests {

    private val getEmployeesPage: GetEmployeesPageFunction = mockk()
    private val cut = SearchEmployeesGraphQLAdapter(getEmployeesPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Employee> = mockk()
        every { getEmployeesPage(EmployeesMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        assertThat(tryToSearchEmployees(query = "query", index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Employee> = mockk()
        every { getEmployeesPage(EmployeesMatchingQuery("query", Pagination.DEFAULT)) } returns page
        assertThat(tryToSearchEmployees(query = "query")).isEqualTo(page)
    }

    private fun tryToSearchEmployees(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchEmployees(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
