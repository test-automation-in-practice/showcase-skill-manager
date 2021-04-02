package skillmanagement.domain.projects.usecases.read

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.project_suggestion_morpheus
import skillmanagement.domain.projects.model.project_suggestion_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.strictJson

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SuggestProjectsRestAdapter::class)
@Import(SuggestProjectsRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/suggest", uriPort = 80)
internal class SuggestProjectsRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val searchIndex: SearchIndex<Project, ProjectId>
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Projects`() {
        every { searchIndex.suggest(any(), any()) } returns emptyList()

        mockMvc
            .post("/api/projects/_suggest") {
                contentType = APPLICATION_JSON
                content = """{"input": "neo"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson { "[]" }
                }
            }
            .andDocument("empty")

        verify { searchIndex.suggest(input = "neo", max = MaxSuggestions.DEFAULT) }
    }

    @Test
    fun `responds with 200 Ok and Projects if there are any`() {
        every { searchIndex.suggest(any(), any()) }
            .returns(listOf(project_suggestion_neo, project_suggestion_morpheus))

        mockMvc
            .post("/api/projects/_suggest?max=5") {
                contentType = APPLICATION_JSON
                content = """{"input": "o"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson {
                        """
                        [
                          {
                            "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
                            "label": "Neo"
                          },
                          {
                            "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
                            "label": "Morpheus"
                          }
                        ]
                        """
                    }
                }
            }
            .andDocument("multiple")

        verify { searchIndex.suggest(input = "o", max = MaxSuggestions(5)) }
    }

}

private class SuggestProjectsRestAdapterTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<Project, ProjectId> = mockk(relaxUnitFun = true)
}
