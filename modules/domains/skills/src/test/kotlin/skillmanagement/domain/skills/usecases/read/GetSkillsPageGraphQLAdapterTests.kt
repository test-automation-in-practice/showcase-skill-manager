package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import skillmanagement.common.graphql.GraphQLClientSideException
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.domain.skills.model.Skill
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetSkillsPageGraphQLAdapterTests {

    private val getSkillsPage: GetSkillsPageFunction = mockk()
    private val cut = GetSkillsPageGraphQLAdapter(getSkillsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Skill> = mockk()
        every { getSkillsPage(AllSkillsQuery(PageIndex(3), PageSize(42))) } returns page
        assertThat(tryToGetSkillsPage(index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `validation errors are translated to GraphQL compatible exception`() {
        assertThrows<GraphQLClientSideException> {
            tryToGetSkillsPage(index = -1)
        }
    }

    private fun tryToGetSkillsPage(index: Int = 0, size: Int = 100) = cut.getSkillsPage(index, size)
}
