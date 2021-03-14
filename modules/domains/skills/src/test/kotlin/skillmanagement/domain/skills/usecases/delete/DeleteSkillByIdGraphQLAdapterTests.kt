package skillmanagement.domain.skills.usecases.delete

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import skillmanagement.common.graphql.GraphQLClientSideException
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteSkillByIdGraphQLAdapterTests {

    private val deleteSkillById: DeleteSkillByIdFunction = mockk()
    private val cut = DeleteSkillByIdGraphQLAdapter(deleteSkillById)

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteSkillById(skill_kotlin.id) } returns SuccessfullyDeleted
        assertThat(tryToDeleteSkill()).isTrue()
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteSkillById(skill_kotlin.id) } returns SkillNotFound
        assertThat(tryToDeleteSkill()).isFalse()
    }

    @Test
    fun `validation errors are translated to GraphQL compatible exception`() {
        assertThrows<GraphQLClientSideException> {
            tryToDeleteSkill(id = "3f7985b9")
        }
    }

    private fun tryToDeleteSkill(id: String = "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b") = cut.deleteSkillById(id)
}
