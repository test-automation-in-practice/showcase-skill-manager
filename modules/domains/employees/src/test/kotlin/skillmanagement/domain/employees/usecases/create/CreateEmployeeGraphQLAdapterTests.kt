package skillmanagement.domain.employees.usecases.create

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.employees.model.employee_creation_data_jane_doe
import skillmanagement.domain.employees.model.employee_creation_data_john_smith
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(CreateEmployeeFunction::class)
@GraphQlTest(CreateEmployeeGraphQLAdapter::class)
internal class CreateEmployeeGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val createEmployee: CreateEmployeeFunction
) : AbstractGraphQlTest() {

    @Test
    fun `delegates creation to business function - max`() {
        every { createEmployee(employee_creation_data_jane_doe) } returns employee_jane_doe
        assertRequestResponse(
            documentPath = "/examples/graphql/createEmployee/jane-doe.graphql",
            responsePath = "/examples/graphql/createEmployee/jane-doe.json"
        )
    }

    @Test
    fun `delegates creation to business function - min`() {
        every { createEmployee(employee_creation_data_john_smith) } returns employee_john_smith
        assertRequestResponse(
            documentPath = "/examples/graphql/createEmployee/john-smith.graphql",
            responsePath = "/examples/graphql/createEmployee/john-smith.json"
        )
    }

}
