package skillmanagement.domain.employees.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(GetEmployeeByIdFunction::class)
@GraphQlTest(GetEmployeeByIdGraphQLAdapter::class)
internal class GetEmployeeByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getEmployeeById: GetEmployeeByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getEmployeeById(employee_jane_doe.id) } returns employee_jane_doe
        assertRequestResponse(
            documentPath = "/examples/graphql/getEmployeeById/jane-doe.graphql",
            responsePath = "/examples/graphql/getEmployeeById/jane-doe.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getEmployeeById(employee_jane_doe.id) } returns null
        assertRequestResponse(
            documentPath = "/examples/graphql/getEmployeeById/jane-doe.graphql",
            responsePath = "/examples/graphql/getEmployeeById/not-found.json"
        )
    }

}
