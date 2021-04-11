package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetEmployeesPageGraphQLAdapterTests {

    private val getEmployeesPage: GetEmployeesPageFunction = mockk()
    private val cut = GetEmployeesPageGraphQLAdapter(getEmployeesPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<EmployeeEntity> = mockk()
        every { getEmployeesPage(AllEmployeesQuery(Pagination(PageIndex(3), PageSize(42)))) } returns page
        assertThat(tryToGetEmployeesPage(index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<EmployeeEntity> = mockk()
        every { getEmployeesPage(AllEmployeesQuery(Pagination.DEFAULT)) } returns page
        assertThat(tryToGetEmployeesPage()).isEqualTo(page)
    }

    private fun tryToGetEmployeesPage(index: Int? = null, size: Int? = null) =
        cut.getEmployeesPage(Pagination(PageIndex.of(index), PageSize.of(size)))
}
