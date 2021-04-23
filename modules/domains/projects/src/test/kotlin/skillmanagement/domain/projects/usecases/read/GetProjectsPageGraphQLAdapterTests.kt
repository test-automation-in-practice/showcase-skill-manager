package skillmanagement.domain.projects.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.asPage
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_representation_morpheus
import skillmanagement.domain.projects.model.project_representation_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetProjectsPageGraphQLAdapterTests {

    private val getProjectsPage: GetProjectsPageFunction = mockk()
    private val cut = GetProjectsPageGraphQLAdapter(getProjectsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page = listOf(project_neo).asPage()
        every { getProjectsPage(AllProjectsQuery(Pagination(PageIndex(3), PageSize(42)))) } returns page
        val actual = tryToGetProjectsPage(index = 3, size = 42)
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(project_representation_neo)))
    }

    @Test
    fun `default values are used when necessary`() {
        val page = listOf(project_morpheus).asPage()
        every { getProjectsPage(AllProjectsQuery(Pagination.DEFAULT)) } returns page
        val actual = tryToGetProjectsPage()
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(project_representation_morpheus)))
    }

    private fun tryToGetProjectsPage(index: Int? = null, size: Int? = null) =
        cut.getProjectsPage(Pagination(PageIndex.of(index), PageSize.of(size)))
}
