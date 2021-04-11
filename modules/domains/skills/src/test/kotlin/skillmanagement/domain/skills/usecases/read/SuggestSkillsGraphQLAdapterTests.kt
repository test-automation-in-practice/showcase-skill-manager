package skillmanagement.domain.skills.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Page
import skillmanagement.common.model.Suggestion
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SuggestSkillsGraphQLAdapterTests {

    private val searchIndex: SearchIndex<SkillEntity, SkillId> = mockk()
    private val cut = SuggestSkillsGraphQLAdapter(searchIndex)

    @Test
    fun `translates and delegates retrieval to business function`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions(42)) } returns page
        assertThat(tryToSuggestSkills(input = "input", max = 42)).isEqualTo(page)
    }

    @Test
    fun `default values are used when necessary`() {
        val page: Page<Suggestion> = mockk()
        every { searchIndex.suggest("input", MaxSuggestions.DEFAULT) } returns page
        assertThat(tryToSuggestSkills(input = "input")).isEqualTo(page)
    }

    private fun tryToSuggestSkills(input: String = "*", max: Int? = null) =
        cut.suggestSkills(input, MaxSuggestions.of(max))

}
