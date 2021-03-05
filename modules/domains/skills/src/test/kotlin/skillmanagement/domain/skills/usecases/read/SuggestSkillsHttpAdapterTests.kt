package skillmanagement.domain.skills.usecases.read

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
import skillmanagement.domain.skills.model.skill_kotlin_suggestion
import skillmanagement.domain.skills.model.skill_python_suggestion
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.strictJson

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SuggestSkillsHttpAdapter::class)
@Import(SuggestSkillsHttpAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/suggest", uriPort = 80)
internal class SuggestSkillsHttpAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val searchIndex: SkillSearchIndex
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Skills`() {
        every { searchIndex.suggestExisting(any(), any()) } returns emptyList()

        mockMvc
            .post("/api/skills/_suggest") {
                contentType = APPLICATION_JSON
                content = """{"input": "kotlin"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson { "[]" }
                }
            }
            .andDocument("empty")

        verify { searchIndex.suggestExisting(input = "kotlin", size = 100) }
    }

    @Test
    fun `responds with 200 Ok and Skills if there are any`() {
        every { searchIndex.suggestExisting(any(), any()) }
            .returns(listOf(skill_kotlin_suggestion, skill_python_suggestion))

        mockMvc
            .post("/api/skills/_suggest?size=5") {
                contentType = APPLICATION_JSON
                content = """{"input": "t"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson {
                        """
                        [
                          {
                            "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                            "label": "Kotlin"
                          },
                          {
                            "id": "6935e550-d041-418a-9070-e37431069232",
                            "label": "Python"
                          }
                        ]
                        """
                    }
                }
            }
            .andDocument("multiple")

        verify { searchIndex.suggestExisting(input = "t", size = 5) }
    }

}

private class SuggestSkillsHttpAdapterTestsConfiguration {
    @Bean
    fun searchIndex(): SkillSearchIndex = mockk(relaxUnitFun = true)
}
