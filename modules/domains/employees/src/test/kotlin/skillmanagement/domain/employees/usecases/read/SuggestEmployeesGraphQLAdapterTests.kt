package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SuggestEmployeesGraphQLAdapterTests {

    private val searchIndex: SearchIndex<EmployeeEntity, EmployeeId> = mockk()
    private val cut = SuggestEmployeesGraphQLAdapter(searchIndex)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions(42)) } returns page
        assertThat(tryToSuggestEmployees(input = "input", max = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions.DEFAULT) } returns page
        assertThat(tryToSuggestEmployees(input = "input")).isEqualTo(page)
    }

    private fun tryToSuggestEmployees(input: String = "*", max: Int? = null) =
        cut.suggestEmployees(input, MaxSuggestions.of(max))

}
