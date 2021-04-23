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
internal class SearchProjectsGraphQLAdapterTests {

    private val getProjectsPage: GetProjectsPageFunction = mockk()
    private val cut = SearchProjectsGraphQLAdapter(getProjectsPage)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page = listOf(project_neo).asPage()
        every { getProjectsPage(ProjectsMatchingQuery("query", Pagination(PageIndex(3), PageSize(42)))) } returns page
        val actual = tryToSearchProjects(query = "query", index = 3, size = 42)
        assertThat(actual).isEqualTo(page.withOtherContent(listOf(project_representation_neo)))
    }

    @Test
    fun `default values are used when necessary`() {
        val page = listOf(project_morpheus).asPage()
        every { getProjectsPage(ProjectsMatchingQuery("query", Pagination.DEFAULT)) } returns page
        val acutal = tryToSearchProjects(query = "query")
        assertThat(acutal).isEqualTo(page.withOtherContent(listOf(project_representation_morpheus)))
    }

    private fun tryToSearchProjects(query: String = "*", index: Int? = null, size: Int? = null) =
        cut.searchProjects(query, Pagination(PageIndex.of(index), PageSize.of(size)))

}
