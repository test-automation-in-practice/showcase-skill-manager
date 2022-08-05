package skillmanagement.domain.employees.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.domain.employees.model.employee_suggestion_jane_doe
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(EmployeeSearchIndex::class)
@GraphQlTest(SuggestEmployeesGraphQLAdapter::class)
internal class SuggestEmployeesGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val searchIndex: EmployeeSearchIndex
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - defaults`() {
        val slot = slot<MaxSuggestions>()
        every { searchIndex.suggest(any(), capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestEmployees/request-defaults.graphql",
            responsePath = "/examples/graphql/suggestEmployees/empty.json"
        )
        assertThat(slot.captured).isEqualTo(MaxSuggestions.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { searchIndex.suggest("jan", MaxSuggestions(10)) } returns
                pageOf(listOf(employee_suggestion_jane_doe))

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestEmployees/request.graphql",
            responsePath = "/examples/graphql/suggestEmployees/found.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - empty`() {
        every { searchIndex.suggest("jan", MaxSuggestions(10)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestEmployees/request.graphql",
            responsePath = "/examples/graphql/suggestEmployees/empty.json"
        )
    }

}
