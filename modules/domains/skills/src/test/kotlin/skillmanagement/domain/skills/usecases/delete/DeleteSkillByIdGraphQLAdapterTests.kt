package skillmanagement.domain.skills.usecases.delete

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(DeleteSkillByIdFunction::class)
@GraphQlTest(DeleteSkillByIdGraphQLAdapter::class)
internal class DeleteSkillByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val deleteSkillById: DeleteSkillByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteSkillById(skill_kotlin.id) } returns true
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteSkillById/kotlin.graphql",
            responsePath = "/examples/graphql/deleteSkillById/deleted.json"
        )
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteSkillById(skill_kotlin.id) } returns false
        assertRequestResponse(
            documentPath = "/examples/graphql/deleteSkillById/kotlin.graphql",
            responsePath = "/examples/graphql/deleteSkillById/not-found.json"
        )
    }

}
