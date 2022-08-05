package skillmanagement.domain.projects.usecases.create

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.projects.model.project_creation_data_morpheus
import skillmanagement.domain.projects.model.project_creation_data_neo
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(CreateProjectFunction::class)
@GraphQlTest(CreateProjectGraphQLAdapter::class)
internal class CreateProjectGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val createProject: CreateProjectFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates creation to business function - max`() {
        every { createProject(project_creation_data_neo) } returns project_neo
        assertRequestResponse(
            documentPath = "/examples/graphql/createProject/neo.graphql",
            responsePath = "/examples/graphql/createProject/neo.json"
        )
    }

    @Test
    fun `translates and delegates creation to business function - min`() {
        every { createProject(project_creation_data_morpheus) } returns project_morpheus
        assertRequestResponse(
            documentPath = "/examples/graphql/createProject/morpheus.graphql",
            responsePath = "/examples/graphql/createProject/morpheus.json"
        )
    }

}
