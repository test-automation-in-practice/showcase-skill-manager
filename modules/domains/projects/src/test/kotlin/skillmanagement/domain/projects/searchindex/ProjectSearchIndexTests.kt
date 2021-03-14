package skillmanagement.domain.projects.searchindex

import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.project
import skillmanagement.domain.projects.model.toSuggestion
import skillmanagement.test.searchindices.SearchIndexIntegrationTest
import java.util.UUID

@SearchIndexIntegrationTest
internal class ProjectSearchIndexTests(client: RestHighLevelClient) {

    private val cut = ProjectSearchIndex(client).apply { enabledTestMode() }

    @BeforeEach
    fun `reset search index`() {
        cut.reset()
    }

    @Test
    fun `entries can be deleted`() {
        val project1 = index(project(label = "Project #1")).id
        val project2 = index(project(label = "Project #2")).id
        val project3 = index(project(label = "Project #3")).id

        assertIndexContainsOnly(project1, project2, project3)

        delete(project2)
        assertIndexContainsOnly(project1, project3)

        delete(project1)
        assertIndexContainsOnly(project3)

        delete(project3)
        assertIndexIsEmpty()
    }

    @Nested
    inner class QueryOperation {

        @Test
        fun `querying an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `query can be used to search project label`() {
            val neo = index(project(label = "Neo", description = "PS4 Pro")).id
            val morpheus = index(project(label = "Morpheus", description = "PS-VR")).id
            val orbis = index(project(label = "Orbis", description = "PS4")).id

            assertThat(query("neo")).containsOnly(neo)
            assertThat(query("label:*or*")).containsOnly(morpheus, orbis)
            assertThat(query("description:PS4")).containsOnly(neo, orbis)
        }

        @Test
        fun `query results are paged`() {
            val project1 = index(project(label = "Project 1")).id
            val project2 = index(project(label = "Project 2")).id
            val project3 = index(project(label = "Project 3")).id
            val project4 = index(project(label = "Project 4")).id
            val project5 = index(project(label = "Project 5")).id

            assertThat(query("project")).containsOnly(project1, project2, project3, project4, project5)

            val page1 = query("project", pageIndex = 0, pageSize = 2)
            val page2 = query("project", pageIndex = 1, pageSize = 2)
            val page3 = query("project", pageIndex = 2, pageSize = 2)

            assertThat(page1).hasSize(2)
            assertThat(page2).hasSize(2)
            assertThat(page3).hasSize(1)

            assertThat(page1 + page2 + page3).containsOnly(project1, project2, project3, project4, project5)
        }

        @Test
        fun `query only uses label by default`() {
            val neo = index(project(label = "Neo", description = "The PlayStation 4 Pro.")).id

            assertThat(query("neo")).containsOnly(neo)
            assertThat(query("playstation")).isEmpty()
        }

    }

    @Nested
    inner class FindAllOperation {

        @Test
        fun `find all for an empty index returns empty result`() {
            assertThat(findAll()).isEmpty()
        }

        @Test
        fun `find all returns all projects paged and sorted by label`() {
            val projectA = index(project(label = "Project A")).id
            val projectB = index(project(label = "Project B")).id
            val projectC = index(project(label = "Project C")).id
            val projectD = index(project(label = "Project D")).id
            val projectE = index(project(label = "Project E")).id
            val projectF = index(project(label = "Project F")).id
            val projectG = index(project(label = "Project G")).id

            assertThat(findAll()).containsExactly(projectA, projectB, projectC, projectD, projectE, projectF, projectG)

            assertThat(findAll(pageIndex = 0, pageSize = 3)).containsExactly(projectA, projectB, projectC)
            assertThat(findAll(pageIndex = 1, pageSize = 3)).containsExactly(projectD, projectE, projectF)
            assertThat(findAll(pageIndex = 2, pageSize = 3)).containsExactly(projectG)
        }

    }

    @Nested
    inner class SuggestOperation {

        @Test
        fun `getting suggestions from an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `getting suggestions returns projects with matching label`() {
            val project1 = index(project(label = "Neo")).toSuggestion()
            val project2 = index(project(label = "Morpheus")).toSuggestion()
            val project3 = index(project(label = "Orbis")).toSuggestion()
            val project4 = index(project(label = "StarLink")).toSuggestion()

            assertThat(suggest("o"))
                .contains(project1, project2, project3)
                .doesNotContain(project4)

            assertThat(suggest("o", max = 2)).hasSize(2)
        }

        @Test
        fun `getting suggestions only uses label data`() {
            val neo = index(project(label = "Neo", description = "The PlayStation 4 Pro.")).toSuggestion()

            assertThat(suggest("neo")).containsOnly(neo)
            assertThat(suggest("playstation")).isEmpty()
        }

    }

    private fun index(project: Project): Project = project.also { cut.index(it) }
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
    ) = cut.query(SimplePagedStringQuery(query, PageIndex(pageIndex), PageSize(pageSize)))

    private fun findAll(
        pageIndex: Int = PageIndex.DEFAULT.toInt(),
        pageSize: Int = PageSize.DEFAULT.toInt()
    ) = cut.findAll(SimplePagedFindAllQuery(PageIndex(pageIndex), PageSize(pageSize)))

    private fun suggest(
        input: String,
        max: Int = MaxSuggestions.DEFAULT.toInt()
    ) = cut.suggest(input, MaxSuggestions(max))

    private data class SimplePagedStringQuery(
        override val queryString: String,
        override val pageIndex: PageIndex,
        override val pageSize: PageSize
    ) : PagedStringQuery

    private data class SimplePagedFindAllQuery(
        override val pageIndex: PageIndex,
        override val pageSize: PageSize
    ) : PagedFindAllQuery
}
