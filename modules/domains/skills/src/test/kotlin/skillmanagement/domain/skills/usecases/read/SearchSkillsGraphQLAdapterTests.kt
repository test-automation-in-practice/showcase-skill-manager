package skillmanagement.domain.skills.usecases.read

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.graphql.AbstractGraphQlTest

@TechnologyIntegrationTest
@MockkBean(GetSkillsPageFunction::class)
@GraphQlTest(SearchSkillsGraphQLAdapter::class)
internal class SearchSkillsGraphQLAdapterTests(
    @Autowired override val graphQlTester: GraphQlTester,
    @Autowired val getSkillsPage: GetSkillsPageFunction
) : AbstractGraphQlTest() {

    @Test
    fun `translates and delegates retrieval to business function - default page`() {
        val slot = slot<SkillsMatchingQuery>()
        every { getSkillsPage(capture(slot)) } returns emptyPage()

        assertRequestResponse(
            documentPath = "/examples/graphql/searchSkills/default-page.graphql",
            responsePath = "/examples/graphql/searchSkills/default-page.json"
        )
        assertThat(slot.captured.pagination).isEqualTo(Pagination.DEFAULT)
    }

    @Test
    fun `translates and delegates retrieval to business function - page 1`() {
        val pagination = Pagination(PageIndex(0), PageSize(10))
        val query = SkillsMatchingQuery("tags:language", pagination)
        every { getSkillsPage(query) } returns
                pageOf(listOf(skill_kotlin, skill_java), index = 0, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/searchSkills/first-page.graphql",
            responsePath = "/examples/graphql/searchSkills/first-page.json"
        )
    }

    @Test
    fun `translates and delegates retrieval to business function - page 2`() {
        val pagination = Pagination(PageIndex(1), PageSize(10))
        val query = SkillsMatchingQuery("tags:language", pagination)
        every { getSkillsPage(query) } returns
                emptyPage(index = 1, size = 10, totalElements = 2)

        assertRequestResponse(
            documentPath = "/examples/graphql/searchSkills/second-page.graphql",
            responsePath = "/examples/graphql/searchSkills/second-page.json"
        )
    }

}
