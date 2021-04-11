package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetSkillsPageGraphQLAdapterTests {

    private val getSkillsPage: GetSkillsPageFunction = mockk()
    private val cut = GetSkillsPageGraphQLAdapter(getSkillsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<SkillEntity> = mockk()
        every { getSkillsPage(AllSkillsQuery(Pagination(PageIndex(3), PageSize(42)))) } returns page
        assertThat(tryToGetSkillsPage(index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<SkillEntity> = mockk()
        every { getSkillsPage(AllSkillsQuery(Pagination.DEFAULT)) } returns page
        assertThat(tryToGetSkillsPage()).isEqualTo(page)
    }

    private fun tryToGetSkillsPage(index: Int? = null, size: Int? = null) =
        cut.getSkillsPage(Pagination(PageIndex.of(index), PageSize.of(size)))
}
