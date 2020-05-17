package skillmanagement.domain.skills.add

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import skillmanagement.test.uuid
import java.time.Clock

@WithMockUser
@TechnologyIntegrationTest
@WebMvcTest(AddSkillHttpAdapter::class)
@Import(TestConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/add", uriPort = 80)
internal class AddSkillHttpAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val addSkill: AddSkill
) {

    val skill = Skill(uuid("14ae4e75-5cf6-4b30-9fc2-7037bd428584"), SkillLabel("Kotlin"))

    @Test
    fun `well formed request leads to correct response`() {
        every { addSkill(SkillLabel("Kotlin")) } returns skill

        mockMvc
            .post("/api/skills") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Kotlin" }"""
            }
            .andExpect {
                status { isCreated }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "14ae4e75-5cf6-4b30-9fc2-7037bd428584",
                          "label": "Kotlin",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/skills/14ae4e75-5cf6-4b30-9fc2-7037bd428584"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("created")
    }

    @Test
    fun `malformed request leads to BAD REQUEST response - illegal value`() {
        mockMvc
            .post("/api/skills") {
                contentType = APPLICATION_JSON
                content = """{ "label": "" }"""
            }
            .andExpect {
                status { isBadRequest }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson {
                        """
                        {
                          "timestamp": "2020-05-08T12:34:56.789Z",
                          "status": 400,
                          "path": "/api/skills",
                          "error": "Bad Request",
                          "message": "Request body validation failed!",
                          "details": [
                            "'Skill Label' [] - must not be blank!"
                          ]
                        }
                        """
                    }
                }
            }
            .andDocument("bad-request")
    }

}

private class TestConfiguration {
    @Bean
    fun addSkill(): AddSkill = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
