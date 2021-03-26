package skillmanagement.domain.skills.searchindex

import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.model.toSuggestion
import skillmanagement.test.searchindices.SearchIndexIntegrationTest
import java.util.UUID

@SearchIndexIntegrationTest
internal class SkillSearchIndexTests(client: RestHighLevelClient) {

    private val cut = SkillSearchIndex(client).apply { enabledTestMode() }

    @BeforeEach
    fun `reset search index`() {
        cut.reset()
    }

    @Test
    fun `entries can be deleted`() {
        val skill1 = index(skill_kotlin).id
        val skill2 = index(skill_java).id
        val skill3 = index(skill_python).id

        assertIndexContainsOnly(skill1, skill2, skill3)

        delete(skill2)
        assertIndexContainsOnly(skill1, skill3)

        delete(skill1)
        assertIndexContainsOnly(skill3)

        delete(skill3)
        assertIndexIsEmpty()
    }

    @Nested
    inner class QueryOperation {

        @Test
        fun `querying an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `query can be used to search skill label and tags`() {
            val kotlin1 = index(skill(label = "Kotlin #1", tags = setOf("language", "cool"))).id
            val python = index(skill(label = "Python", tags = setOf("language", "scripting"))).id
            val java = index(skill(label = "Java", tags = setOf("language"))).id
            val kotlin2 = index(skill(label = "Kotlin #2")).id

            assertThat(query("kotlin")).containsOnly(kotlin1, kotlin2)
            assertThat(query("label:*o*")).containsOnly(kotlin1, kotlin2, python)
            assertThat(query("tags:language")).containsOnly(kotlin1, python, java)
            assertThat(query("tags:cool")).containsOnly(kotlin1)
            assertThat(query("label:kotlin AND tags:language")).containsOnly(kotlin1)
        }

        @Test
        fun `query results are paged`() {
            val angular1 = index(skill(label = "Angular 1")).id
            val angular2 = index(skill(label = "Angular 2")).id
            val angular3 = index(skill(label = "Angular 3")).id
            val angular4 = index(skill(label = "Angular 4")).id
            val angular5 = index(skill(label = "Angular 5")).id

            assertThat(query("angular")).containsOnly(angular1, angular2, angular3, angular4, angular5)

            val page1 = query("angular", pageIndex = 0, pageSize = 2)
            val page2 = query("angular", pageIndex = 1, pageSize = 2)
            val page3 = query("angular", pageIndex = 2, pageSize = 2)

            assertThat(page1).hasSize(2)
            assertThat(page2).hasSize(2)
            assertThat(page3).hasSize(1)

            assertThat(page1 + page2 + page3).containsOnly(angular1, angular2, angular3, angular4, angular5)
        }

        @Test
        fun `query only uses label by default`() {
            val swift = index(skill(label = "Swift", tags = setOf("language"))).id

            assertThat(query("swift")).containsOnly(swift)
            assertThat(query("language")).isEmpty()
        }

        @TestFactory
        fun `properties can be queried`(): List<DynamicTest> = listOf(
            "label:value" to skill(
                label = "value",
                description = "unknown",
                tags = setOf("unknown", "unknown")
            ),
            "description:value" to skill(
                label = "unknown",
                description = "value",
                tags = setOf("unknown", "unknown")
            ),
            "tags:value" to skill(
                label = "unknown",
                description = "unknown",
                tags = setOf("value", "unknown")
            )
        ).map { (query, skill) ->
            dynamicTest(query) {
                val skillId = index(skill).id
                assertThat(query(query)).containsOnly(skillId)
            }
        }

    }

    @Nested
    inner class FindAllOperation {

        @Test
        fun `find all for an empty index returns empty result`() {
            assertThat(findAll()).isEmpty()
        }

        @Test
        fun `find all returns all skills paged and sorted by label`() {
            val skillA = index(skill(label = "Skill A")).id
            val skillB = index(skill(label = "Skill B")).id
            val skillC = index(skill(label = "Skill C")).id
            val skillD = index(skill(label = "Skill D")).id
            val skillE = index(skill(label = "Skill E")).id
            val skillF = index(skill(label = "Skill F")).id
            val skillG = index(skill(label = "Skill G")).id

            assertThat(findAll()).containsExactly(skillA, skillB, skillC, skillD, skillE, skillF, skillG)

            assertThat(findAll(pageIndex = 0, pageSize = 3)).containsExactly(skillA, skillB, skillC)
            assertThat(findAll(pageIndex = 1, pageSize = 3)).containsExactly(skillD, skillE, skillF)
            assertThat(findAll(pageIndex = 2, pageSize = 3)).containsExactly(skillG)
        }

    }

    @Nested
    inner class SuggestOperation {

        @Test
        fun `getting suggestions from an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `getting suggestions returns skills with matching label`() {
            val skill1 = index(skill(label = "The Kotlin")).toSuggestion()
            val skill2 = index(skill(label = "Kotlin #1")).toSuggestion()
            val skill3 = index(skill(label = "Kotlin #2")).toSuggestion()
            val skill4 = index(skill(label = "Python")).toSuggestion()

            assertThat(suggest("ko"))
                .contains(skill1, skill2, skill3)
                .doesNotContain(skill4)

            assertThat(suggest("ko", max = 2)).hasSize(2)
        }

        @Test
        fun `getting suggestions only uses label data`() {
            val swift = index(skill(label = "Swift", tags = setOf("language"))).toSuggestion()

            assertThat(suggest("swift")).containsOnly(swift)
            assertThat(suggest("language")).isEmpty()
        }

    }

    private fun index(skill: Skill): Skill = skill.also { cut.index(it) }
    private fun delete(vararg ids: UUID) = ids.forEach(cut::deleteById)

    private fun assertIndexContainsOnly(vararg ids: UUID) {
        assertThat(findAll()).containsExactly(*ids)
    }

    private fun assertIndexIsEmpty() {
        assertThat(findAll()).isEmpty()
    }

    private fun query(
        query: String,
        pageIndex: Int = PageIndex.DEFAULT.toInt(),
        pageSize: Int = PageSize.DEFAULT.toInt()
    ) = cut.query(SimplePagedStringQuery(query, Pagination(PageIndex(pageIndex), PageSize(pageSize))))

    private fun findAll(
        pageIndex: Int = PageIndex.DEFAULT.toInt(),
        pageSize: Int = PageSize.DEFAULT.toInt()
    ) = cut.findAll(SimplePagedFindAllQuery(Pagination(PageIndex(pageIndex), PageSize(pageSize))))

    private fun suggest(
        input: String,
        max: Int = MaxSuggestions.DEFAULT.toInt()
    ) = cut.suggest(input, MaxSuggestions(max))

    private data class SimplePagedStringQuery(
        override val queryString: String,
        override val pagination: Pagination,
    ) : PagedStringQuery

    private data class SimplePagedFindAllQuery(
        override val pagination: Pagination
    ) : PagedFindAllQuery
}
