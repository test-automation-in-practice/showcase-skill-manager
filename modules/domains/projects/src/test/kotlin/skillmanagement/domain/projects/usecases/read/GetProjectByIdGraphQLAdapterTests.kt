package skillmanagement.domain.projects.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(GetProjectByIdFunction::class)
@GraphQlTest(GetProjectByIdGraphQLAdapter::class)
internal class GetProjectByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getProjectById: GetProjectByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getProjectById(project_neo.id) } returns project_neo
        assertRequestResponse(
            documentPath = "/examples/graphql/getProjectById/neo.graphql",
            responsePath = "/examples/graphql/getProjectById/neo.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getProjectById(project_neo.id) } returns null
        assertRequestResponse(
            documentPath = "/examples/graphql/getProjectById/neo.graphql",
            responsePath = "/examples/graphql/getProjectById/not-found.json"
        )
    }

}
