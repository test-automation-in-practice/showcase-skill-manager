package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import skillmanagement.common.graphql.GraphQLClientSideException
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetSkillByIdGraphQLAdapterTests {

    private val getSkillById: GetSkillByIdFunction = mockk()
    private val cut = GetSkillByIdGraphQLAdapter(getSkillById)

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getSkillById(skill_kotlin.id) } returns skill_kotlin
        assertThat(tryToGetSkill()).isEqualTo(skill_kotlin)
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getSkillById(skill_kotlin.id) } returns null
        assertThat(tryToGetSkill()).isNull()
    }

    @Test
    fun `validation errors are translated to GraphQL compatible exception`() {
        assertThrows<GraphQLClientSideException> {
            tryToGetSkill(id = "3f7985b9")
        }
    }

    private fun tryToGetSkill(id: String = "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b") = cut.getSkillById(id)
}
