package skillmanagement.domain.skills.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.domain.skills.model.skill_suggestion_kotlin
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(SkillSearchIndex::class)
@GraphQlTest(SuggestSkillsGraphQLAdapter::class)
internal class SuggestSkillsGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val searchIndex: SkillSearchIndex
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - defaults`() {
        val slot = slot<MaxSuggestions>()
        every { searchIndex.suggest(any(), capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestSkills/request-defaults.graphql",
            responsePath = "/examples/graphql/suggestSkills/empty.json"
        )
        assertThat(slot.captured).isEqualTo(MaxSuggestions.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { searchIndex.suggest("ko", MaxSuggestions(10)) } returns
                pageOf(listOf(skill_suggestion_kotlin))

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestSkills/request.graphql",
            responsePath = "/examples/graphql/suggestSkills/found.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - empty`() {
        every { searchIndex.suggest("ko", MaxSuggestions(10)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestSkills/request.graphql",
            responsePath = "/examples/graphql/suggestSkills/empty.json"
        )
    }

}
