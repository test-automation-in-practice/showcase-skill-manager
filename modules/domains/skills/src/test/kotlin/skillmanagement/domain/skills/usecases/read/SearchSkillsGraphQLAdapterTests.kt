package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.pageOf
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_representation_java
import skillmanagement.domain.skills.model.skill_representation_kotlin
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SearchSkillsGraphQLAdapterTests {

    private val getSkillsPage: GetSkillsPageFunction = mockk()
    private val cut = SearchSkillsGraphQLAdapter(getSkillsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page = pageOf(listOf(skill_kotlin), 3, 42, totalElements = (3 * 42) + 1)
        every { getSkillsPage(SkillsMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        val actual = tryToSearchSkills(query = "query", index = 3, size = 42)
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(skill_representation_kotlin)))
    }

    @Test
    fun `default values are used when necessary`() {
        val page = pageOf(listOf(skill_java))
        every { getSkillsPage(SkillsMatchingQuery("query", Pagination.DEFAULT)) } returns page
        val actual = tryToSearchSkills(query = "query")
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(skill_representation_java)))
    }

    private fun tryToSearchSkills(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchSkills(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
