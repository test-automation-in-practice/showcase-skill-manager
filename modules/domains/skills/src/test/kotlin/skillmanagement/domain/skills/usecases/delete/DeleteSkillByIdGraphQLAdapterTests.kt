package skillmanagement.domain.skills.usecases.delete

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteSkillByIdGraphQLAdapterTests {

    private val deleteSkillById: DeleteSkillByIdFunction = mockk()
    private val cut = DeleteSkillByIdGraphQLAdapter(deleteSkillById)

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteSkillById(skill_kotlin.id) } returns true
        assertThat(tryToDeleteSkill()).isTrue()
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteSkillById(skill_kotlin.id) } returns false
        assertThat(tryToDeleteSkill()).isFalse()
    }

    private fun tryToDeleteSkill(id: String = "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b") = cut.deleteSkillById(id)
}
