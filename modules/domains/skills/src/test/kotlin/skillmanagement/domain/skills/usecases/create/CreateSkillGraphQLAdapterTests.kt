package skillmanagement.domain.skills.usecases.create

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.domain.skills.model.skill_creation_data_kotlin
import skillmanagement.domain.skills.model.skill_creation_data_python
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(CreateSkillFunction::class)
@GraphQlTest(CreateSkillGraphQLAdapter::class)
internal class CreateSkillGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val createSkill: CreateSkillFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates creation to business function - max`() {
        every { createSkill(skill_creation_data_kotlin) } returns skill_kotlin
        assertRequestResponse(
            documentPath = "/examples/graphql/createSkill/kotlin.graphql",
            responsePath = "/examples/graphql/createSkill/kotlin.json"
        )
    }

    @Test
    fun `translates and delegates creation to business function - min`() {
        every { createSkill(skill_creation_data_python) } returns skill_python
        assertRequestResponse(
            documentPath = "/examples/graphql/createSkill/python.graphql",
            responsePath = "/examples/graphql/createSkill/python.json"
        )
    }

}
