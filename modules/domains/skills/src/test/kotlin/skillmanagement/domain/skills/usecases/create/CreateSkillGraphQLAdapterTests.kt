package skillmanagement.domain.skills.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import skillmanagement.common.graphql.GraphQLClientSideException
import skillmanagement.domain.skills.model.skill
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class CreateSkillGraphQLAdapterTests {

    private val createSkill: CreateSkillFunction = mockk()
    private val cut = CreateSkillGraphQLAdapter(createSkill)

    @Test
    fun `translates and delegates creation to business function`() {
        val skill = skill(
            label = "label",
            description = "description",
            tags = listOf("abc", "def")
        )
        every { createSkill(skill.label, skill.description, skill.tags) } returns skill
        assertThat(tryToCreateSkill()).isEqualTo(skill)
    }

    @Test
    fun `validation errors are translated to GraphQL compatible exception`() {
        assertThrows<GraphQLClientSideException> {
            tryToCreateSkill(label = "")
        }
    }

    private fun tryToCreateSkill(
        label: String = "label",
        description: String = "description",
        tags: List<String> = listOf("abc", "def")
    ) = cut.createSkill(label = label, description = description, tags = tags)
}
