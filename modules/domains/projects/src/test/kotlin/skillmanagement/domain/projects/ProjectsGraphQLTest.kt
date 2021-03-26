package skillmanagement.domain.projects

import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.mockk.every
import io.mockk.mockk
import mu.KotlinLogging.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus.OK
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.project_creation_data_morpheus
import skillmanagement.domain.projects.model.project_creation_data_neo
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_suggestion_neo
import skillmanagement.domain.projects.model.project_orbis
import skillmanagement.domain.projects.usecases.create.CreateProjectFunction
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdFunction
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import skillmanagement.domain.projects.usecases.read.AllProjectsQuery
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import skillmanagement.domain.projects.usecases.read.GetProjectsPageFunction
import skillmanagement.domain.projects.usecases.read.ProjectsMatchingQuery
import skillmanagement.test.ResetMocksAfterEachTest

@GraphQLTest
@ResetMocksAfterEachTest
@Import(TestConfiguration::class)
internal class ProjectsGraphQLTest(
    @Autowired private val graphQL: GraphQLTestTemplate
) {

    private val log = logger {}

    @Test
    fun `create project - max data`(@Autowired createProject: CreateProjectFunction) {
        every { createProject(project_creation_data_neo) } returns project_neo
        assertRequestResponse(
            request = "/graphql/createProject/neo.graphql",
            expectedResponseBody = "/graphql/createProject/neo.json"
        )
    }

    @Test
    fun `create project - min data`(@Autowired createProject: CreateProjectFunction) {
        every { createProject(project_creation_data_morpheus) } returns project_morpheus
        assertRequestResponse(
            request = "/graphql/createProject/morpheus.graphql",
            expectedResponseBody = "/graphql/createProject/morpheus.json"
        )
    }

    @Test
    fun `get project by id - max data`(@Autowired getProjectById: GetProjectByIdFunction) {
        every { getProjectById(project_neo.id) } returns project_neo
        assertRequestResponse(
            request = "/graphql/getProjectById/neo.graphql",
            expectedResponseBody = "/graphql/getProjectById/neo.json"
        )
    }

    @Test
    fun `get project by id - min data`(@Autowired getProjectById: GetProjectByIdFunction) {
        every { getProjectById(project_morpheus.id) } returns project_morpheus
        assertRequestResponse(
            request = "/graphql/getProjectById/morpheus.graphql",
            expectedResponseBody = "/graphql/getProjectById/morpheus.json"
        )
    }

    @Test
    fun `get project by id - not found`(@Autowired getProjectById: GetProjectByIdFunction) {
        every { getProjectById(project_orbis.id) } returns null
        assertRequestResponse(
            request = "/graphql/getProjectById/orbis.graphql",
            expectedResponseBody = "/graphql/getProjectById/not-found.json"
        )
    }

    @Test
    fun `get projects page - found`(@Autowired getProjectsPage: GetProjectsPageFunction) {
        every { getProjectsPage(AllProjectsQuery(Pagination(PageIndex(0), PageSize(10)))) }
            .returns(Page(listOf(project_neo, project_morpheus), 0, 10, 2))
        assertRequestResponse(
            request = "/graphql/getProjectsPage/first-page.graphql",
            expectedResponseBody = "/graphql/getProjectsPage/found.json"
        )
    }

    @Test
    fun `get projects page - empty`(@Autowired getProjectsPage: GetProjectsPageFunction) {
        every { getProjectsPage(AllProjectsQuery(Pagination(PageIndex(1), PageSize(10)))) }
            .returns(Page(emptyList(), 1, 10, 2))
        assertRequestResponse(
            request = "/graphql/getProjectsPage/second-page.graphql",
            expectedResponseBody = "/graphql/getProjectsPage/empty.json"
        )
    }

    @Test
    fun `search projects - found`(@Autowired getProjectsPage: GetProjectsPageFunction) {
        val query = ProjectsMatchingQuery("description:playstation", Pagination(PageIndex(0), PageSize(10)))
        every { getProjectsPage(query) } returns Page(listOf(project_neo, project_morpheus), 0, 10, 2)
        assertRequestResponse(
            request = "/graphql/searchProjects/first-page.graphql",
            expectedResponseBody = "/graphql/searchProjects/found.json"
        )
    }

    @Test
    fun `search projects - empty`(@Autowired getProjectsPage: GetProjectsPageFunction) {
        val query = ProjectsMatchingQuery("description:playstation", Pagination(PageIndex(1), PageSize(10)))
        every { getProjectsPage(query) } returns Page(emptyList(), 1, 10, 2)
        assertRequestResponse(
            request = "/graphql/searchProjects/second-page.graphql",
            expectedResponseBody = "/graphql/searchProjects/empty.json"
        )
    }

    @Test
    fun `suggest projects - found`(@Autowired searchIndex: SearchIndex<Project>) {
        every { searchIndex.suggest("ne", MaxSuggestions(10)) } returns listOf(project_suggestion_neo)
        assertRequestResponse(
            request = "/graphql/suggestProjects/request.graphql",
            expectedResponseBody = "/graphql/suggestProjects/found.json"
        )
    }

    @Test
    fun `suggest projects - empty`(@Autowired searchIndex: SearchIndex<Project>) {
        every { searchIndex.suggest("ne", MaxSuggestions(10)) } returns emptyList()
        assertRequestResponse(
            request = "/graphql/suggestProjects/request.graphql",
            expectedResponseBody = "/graphql/suggestProjects/empty.json"
        )
    }

    @Test
    fun `delete project by ID - deleted`(@Autowired deleteProjectById: DeleteProjectByIdFunction) {
        every { deleteProjectById(project_neo.id) } returns SuccessfullyDeleted
        assertRequestResponse(
            request = "/graphql/deleteProjectById/neo.graphql",
            expectedResponseBody = "/graphql/deleteProjectById/deleted.json"
        )
    }

    @Test
    fun `delete project by ID - not found`(@Autowired deleteProjectById: DeleteProjectByIdFunction) {
        every { deleteProjectById(project_neo.id) } returns ProjectNotFound
        assertRequestResponse(
            request = "/graphql/deleteProjectById/neo.graphql",
            expectedResponseBody = "/graphql/deleteProjectById/not-found.json"
        )
    }

    private fun assertRequestResponse(request: String, expectedResponseBody: String) {
        val response = graphQL.postForResource(request)
        log.info { response.rawResponse }
        assertThat(response.statusCode).isEqualTo(OK)
        response.assertThatJsonContent()
            .isStrictlyEqualToJson(ClassPathResource(expectedResponseBody))
    }

}

private class TestConfiguration {

    @Bean
    fun createProjectFunction(): CreateProjectFunction = mockk()

    @Bean
    fun deleteProjectByIdFunction(): DeleteProjectByIdFunction = mockk()

    @Bean
    fun getProjectByIdFunction(): GetProjectByIdFunction = mockk()

    @Bean
    fun getProjectsFunction(): GetProjectsPageFunction = mockk()

    @Bean
    fun searchIndex(): SearchIndex<Project> = mockk()

}
