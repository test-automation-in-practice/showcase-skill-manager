package skillmanagement.domain.skills.usecases.read

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
@MockkBean(GetSkillByIdFunction::class)
@GraphQlTest(GetSkillByIdGraphQLAdapter::class)
internal class GetSkillByIdGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getSkillById: GetSkillByIdFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getSkillById(skill_kotlin.id) } returns skill_kotlin
        assertRequestResponse(
            documentPath = "/examples/graphql/getSkillById/kotlin.graphql",
            responsePath = "/examples/graphql/getSkillById/kotlin.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getSkillById(skill_kotlin.id) } returns null
        assertRequestResponse(
            documentPath = "/examples/graphql/getSkillById/kotlin.graphql",
            responsePath = "/examples/graphql/getSkillById/not-found.json"
        )
    }

}
