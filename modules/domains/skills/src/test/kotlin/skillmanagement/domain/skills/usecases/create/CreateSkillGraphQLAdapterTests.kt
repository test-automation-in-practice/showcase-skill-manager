package skillmanagement.domain.skills.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.skill_creation_data_kotlin
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class CreateSkillGraphQLAdapterTests {

    private val createSkill: CreateSkillFunction = mockk()
    private val cut = CreateSkillGraphQLAdapter(createSkill)

    @Test
    fun `delegates creation to business function`() {
        every { createSkill(skill_creation_data_kotlin) } returns skill_kotlin
        assertThat(cut.createSkill(skill_creation_data_kotlin)).isEqualTo(skill_kotlin)
    }

}
