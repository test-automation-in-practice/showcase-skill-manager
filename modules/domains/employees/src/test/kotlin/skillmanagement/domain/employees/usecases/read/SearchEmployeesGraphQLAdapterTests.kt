package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.asPage
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.model.employee_representation_jane_doe
import skillmanagement.domain.employees.model.employee_representation_john_smith
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SearchEmployeesGraphQLAdapterTests {

    private val getEmployeesPage: GetEmployeesPageFunction = mockk()
    private val cut = SearchEmployeesGraphQLAdapter(getEmployeesPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page = listOf(employee_jane_doe).asPage()
        every { getEmployeesPage(EmployeesMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        val actual = tryToSearchEmployees(query = "query", index = 3, size = 42)
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(employee_representation_jane_doe)))
    }

    @Test
    fun `default values are used when necessary`() {
        val page = listOf(employee_john_smith).asPage()
        every { getEmployeesPage(EmployeesMatchingQuery("query", Pagination.DEFAULT)) } returns page
        val actual = tryToSearchEmployees(query = "query")
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(employee_representation_john_smith)))
    }

    private fun tryToSearchEmployees(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchEmployees(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
