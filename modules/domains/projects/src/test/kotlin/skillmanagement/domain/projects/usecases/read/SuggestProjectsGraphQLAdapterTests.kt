package skillmanagement.domain.projects.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SuggestProjectsGraphQLAdapterTests {

    private val searchIndex: SearchIndex<Project, ProjectId> = mockk()
    private val cut = SuggestProjectsGraphQLAdapter(searchIndex)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions(42)) } returns page
        assertThat(tryToSuggestProjects(input = "input", max = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions.DEFAULT) } returns page
        assertThat(tryToSuggestProjects(input = "input")).isEqualTo(page)
    }

    private fun tryToSuggestProjects(input: String = "*", max: Int? = null) =
        cut.suggestProjects(input, MaxSuggestions.of(max))

}
