package skillmanagement.domain.projects.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.domain.projects.model.Project
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SearchProjectsGraphQLAdapterTests {

    private val getProjectsPage: GetProjectsPageFunction = mockk()
    private val cut = SearchProjectsGraphQLAdapter(getProjectsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Project> = mockk()
        every { getProjectsPage(ProjectsMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        assertThat(tryToSearchProjects(query = "query", index = 3, size = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Project> = mockk()
        every { getProjectsPage(ProjectsMatchingQuery("query", Pagination.DEFAULT)) } returns page
        assertThat(tryToSearchProjects(query = "query")).isEqualTo(page)
    }

    private fun tryToSearchProjects(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchProjects(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
