package skillmanagement.domain.employees.usecases.delete

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
@MockkBean(DeleteEmployeeByIdFunction::class)
@GraphQlTest(DeleteEmployeeByIdGraphQLAdapter::class)
internal class DeleteEmployeeByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val deleteEmployeeById: DeleteEmployeeByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteEmployeeById(employee_jane_doe.id) } returns true
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteEmployeeById/jane-doe.graphql",
            responsePath = "/examples/graphql/deleteEmployeeById/deleted.json"
        )
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteEmployeeById(employee_jane_doe.id) } returns false
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteEmployeeById/jane-doe.graphql",
            responsePath = "/examples/graphql/deleteEmployeeById/not-found.json"
        )
    }

}
