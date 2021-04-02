package skillmanagement.domain.projects.usecases.read

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.common.model.pageOf
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetProjectsPageFunctionTests {

    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction = mockk()
    private val searchIndex: SearchIndex<Project, ProjectId> = mockk()
    private val findProjects = GetProjectsPageFunction(getProjectsFromDataStore, searchIndex)

    @Test
    fun `AllProjectsQuery just returns all projects from database`() {
        val query = AllProjectsQuery()
        val ids = listOf(project_neo.id, project_morpheus.id)
        every { searchIndex.findAll(query) } returns pageOf(ids)
        every { getProjectsFromDataStore(ids) } returns projectMap(project_morpheus, project_neo)

        findProjects(query) shouldBe pageOf(listOf(project_neo, project_morpheus))
    }

    @Test
    fun `ProjectsMatchingQuery gets IDs from search index and then corresponding projects from database`() {
        val query = ProjectsMatchingQuery(queryString = "kotlin")
        val ids = listOf(project_neo.id)
        every { searchIndex.query(query) } returns pageOf(ids)
        every { getProjectsFromDataStore(ids) } returns projectMap(project_neo)

        findProjects(query) shouldBe pageOf(listOf(project_neo))
    }

    private fun projectMap(vararg projects: Project) = projects.map { it.id to it }.toMap()

}
