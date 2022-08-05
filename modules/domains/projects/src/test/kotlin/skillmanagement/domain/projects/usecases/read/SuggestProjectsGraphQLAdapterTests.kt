package skillmanagement.domain.projects.usecases.read

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
import skillmanagement.domain.projects.model.project_suggestion_neo
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(ProjectSearchIndex::class)
@GraphQlTest(SuggestProjectsGraphQLAdapter::class)
internal class SuggestProjectsGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val searchIndex: ProjectSearchIndex
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - defaults`() {
        val slot = slot<MaxSuggestions>()
        every { searchIndex.suggest(any(), capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestProjects/request-defaults.graphql",
            responsePath = "/examples/graphql/suggestProjects/empty.json"
        )
        assertThat(slot.captured).isEqualTo(MaxSuggestions.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { searchIndex.suggest("ne", MaxSuggestions(10)) } returns
                pageOf(listOf(project_suggestion_neo))

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestProjects/request.graphql",
            responsePath = "/examples/graphql/suggestProjects/found.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - empty`() {
        every { searchIndex.suggest("ne", MaxSuggestions(10)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/suggestProjects/request.graphql",
            responsePath = "/examples/graphql/suggestProjects/empty.json"
        )
    }

}
