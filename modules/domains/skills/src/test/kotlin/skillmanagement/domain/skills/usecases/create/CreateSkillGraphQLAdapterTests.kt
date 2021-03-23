package skillmanagement.domain.skills.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.skill
import skillmanagement.domain.skills.usecases.create.CreateSkillGraphQLAdapter.SkillInput
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

    private fun tryToCreateSkill(
        label: String = "label",
        description: String = "description",
        tags: Set<String> = setOf("abc", "def")
    ) = cut.createSkill(
        SkillInput(
            label = SkillLabel(label),
            description = SkillDescription(description),
            tags = tags.map(::Tag).toSet()
        )
    )
}
