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
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill_suggestion_kotlin
import skillmanagement.domain.skills.model.skill_suggestion_python
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.strictJson

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SuggestSkillsRestAdapter::class)
@Import(SuggestSkillsRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/suggest", uriPort = 80)
internal class SuggestSkillsRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val searchIndex: SearchIndex<Skill>
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Skills`() {
        every { searchIndex.suggest(any(), any()) } returns emptyList()

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

        verify { searchIndex.suggest(input = "kotlin", max = MaxSuggestions(100)) }
    }

    @Test
    fun `responds with 200 Ok and Skills if there are any`() {
        every { searchIndex.suggest(any(), any()) }
            .returns(listOf(skill_suggestion_kotlin, skill_suggestion_python))

        mockMvc
            .post("/api/skills/_suggest?max=5") {
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

        verify { searchIndex.suggest(input = "t", max = MaxSuggestions(5)) }
    }

}

private class SuggestSkillsRestAdapterTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<Skill> = mockk(relaxUnitFun = true)
}
