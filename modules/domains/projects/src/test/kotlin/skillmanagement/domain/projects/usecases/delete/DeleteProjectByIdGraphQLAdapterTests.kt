package skillmanagement.domain.projects.usecases.delete

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
@MockkBean(DeleteProjectByIdFunction::class)
@GraphQlTest(DeleteProjectByIdGraphQLAdapter::class)
internal class DeleteProjectByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val deleteProjectById: DeleteProjectByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteProjectById(project_neo.id) } returns true
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteProjectById/neo.graphql",
            responsePath = "/examples/graphql/deleteProjectById/deleted.json"
        )
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteProjectById(project_neo.id) } returns false
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteProjectById/neo.graphql",
            responsePath = "/examples/graphql/deleteProjectById/not-found.json"
        )
    }

}
