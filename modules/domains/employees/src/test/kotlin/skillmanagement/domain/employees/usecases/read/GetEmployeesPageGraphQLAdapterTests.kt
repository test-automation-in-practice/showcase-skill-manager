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
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(GetEmployeesPageFunction::class)
@GraphQlTest(GetEmployeesPageGraphQLAdapter::class)
internal class GetEmployeesPageGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getEmployeesPage: GetEmployeesPageFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - default page`() {
        val slot = slot<AllEmployeesQuery>()
        every { getEmployeesPage(capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/getEmployeesPage/default-page.graphql",
            responsePath = "/examples/graphql/getEmployeesPage/default-page.json"
        )
        assertThat(slot.captured.pagination).isEqualTo(Pagination.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - page 1`() {
        val expectedQuery = AllEmployeesQuery(Pagination(PageIndex(0), PageSize(10)))
        every { getEmployeesPage(expectedQuery) } returns
                pageOf(listOf(employee_jane_doe, employee_john_smith), index = 0, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/getEmployeesPage/first-page.graphql",
            responsePath = "/examples/graphql/getEmployeesPage/first-page.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - page 2`() {
        val expectedQuery = AllEmployeesQuery(Pagination(PageIndex(1), PageSize(10)))
        every { getEmployeesPage(expectedQuery) } returns
                emptyPage(index = 1, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/getEmployeesPage/second-page.graphql",
            responsePath = "/examples/graphql/getEmployeesPage/second-page.json"
        )
    }

}
