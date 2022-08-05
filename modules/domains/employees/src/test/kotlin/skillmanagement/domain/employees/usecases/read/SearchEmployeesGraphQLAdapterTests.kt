package skillmanagement.domain.employees.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(GetEmployeesPageFunction::class)
@GraphQlTest(SearchEmployeesGraphQLAdapter::class)
internal class SearchEmployeesGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getEmployeesPage: GetEmployeesPageFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - default page`() {
        val slot = slot<EmployeesMatchingQuery>()
        every { getEmployeesPage(capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/searchEmployees/default-page.graphql",
            responsePath = "/examples/graphql/searchEmployees/default-page.json"
        )
        assertThat(slot.captured.pagination).isEqualTo(Pagination.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - page 1`() {
        val pagination = Pagination(PageIndex(0), PageSize(10))
        val query = EmployeesMatchingQuery("name:doe", pagination)
        every { getEmployeesPage(query) } returns
                pageOf(listOf(employee_jane_doe, employee_john_doe), index = 0, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/searchEmployees/first-page.graphql",
            responsePath = "/examples/graphql/searchEmployees/first-page.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - page 2`() {
        val pagination = Pagination(PageIndex(1), PageSize(10))
        val query = EmployeesMatchingQuery("name:doe", pagination)
        every { getEmployeesPage(query) } returns
                emptyPage(index = 1, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/searchEmployees/second-page.graphql",
            responsePath = "/examples/graphql/searchEmployees/second-page.json"
        )
    }

}
