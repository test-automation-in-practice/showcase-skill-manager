package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.domain.skills.model.Skill
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SearchSkillsGraphQLAdapterTests {

    private val getSkillsPage: GetSkillsPageFunction = mockk()
    private val cut = SearchSkillsGraphQLAdapter(getSkillsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Skill> = mockk()
        every { getSkillsPage(SkillsMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        assertThat(tryToSearchSkills(query = "query", index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Skill> = mockk()
        every { getSkillsPage(SkillsMatchingQuery("query", Pagination.DEFAULT)) } returns page
        assertThat(tryToSearchSkills(query = "query")).isEqualTo(page)
    }

    private fun tryToSearchSkills(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchSkills(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
