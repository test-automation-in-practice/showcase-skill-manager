package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.graphql.Pagination
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
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
        every { getSkillsPage(SkillsMatchingQuery(PageIndex(3), PageSize(42), "query")) } returns page
        assertThat(tryToSearchSkills(query = "query", index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Skill> = mockk()
        every { getSkillsPage(SkillsMatchingQuery(PageIndex.DEFAULT, PageSize.DEFAULT, "query")) } returns page
        assertThat(tryToSearchSkills(query = "query")).isEqualTo(page)
    }

    private fun tryToSearchSkills(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchSkills(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
