package skillmanagement.domain.skills.update

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import skillmanagement.common.ApplyPatch
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.skill_kotlin
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SuccessfullyUpdated
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@WithMockUser
@TechnologyIntegrationTest
@WebMvcTest(UpdateSkillByIdHttpAdapter::class)
@Import(TestConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/update", uriPort = 80)
internal class UpdateSkillByIdHttpAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val updateSkillById: UpdateSkillById
) {

    val skill = skill_kotlin

    @Test
    fun `PUT - when updating a complete skill it's updated state is returned`() {
        every { updateSkillById(skill.id, any()) } answers {
            val block: (Skill) -> (Skill) = secondArg()
            SuccessfullyUpdated(block(skill))
        }

        mockMvc
            .put("/api/skills/${skill.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Kotlin (Language)" }"""
            }
            .andExpect {
                status { isOk }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                          "label": "Kotlin (Language)",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                            },
                            "delete": {
                              "href": "http://localhost/api/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("put/updated")
    }

    @Test
    fun `PUT - when updating a non-existing skill the response will be a 404`() {
        every { updateSkillById(skill.id, any()) } returns SkillNotFound

        mockMvc
            .put("/api/skills/${skill.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Kotlin (Language)" }"""
            }
            .andExpect {
                status { isNotFound }
                content { string("") }
            }
            .andDocument("put/not-found")
    }

    @Test
    fun `PATCH - JSON Patch can be used to update properties of a skill - label`() {
        every { updateSkillById(skill.id, any()) } answers {
            val block: (Skill) -> (Skill) = secondArg()
            SuccessfullyUpdated(block(skill))
        }

        mockMvc
            .patch("/api/skills/${skill.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Kotlin (Language)"
                      }
                    ]
                    """
            }
            .andExpect {
                status { isOk }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                          "label": "Kotlin (Language)",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                            },
                            "delete": {
                              "href": "http://localhost/api/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("patch/updated")
    }

    @Test
    fun `PATCH - when updating a non-existing skill the response will be a 404`() {
        every { updateSkillById(skill.id, any()) } returns SkillNotFound

        mockMvc
            .patch("/api/skills/${skill.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Kotlin (Language)"
                      }
                    ]
                    """
            }
            .andExpect {
                status { isNotFound }
                content { string("") }
            }
            .andDocument("patch/not-found")
    }

}

@Import(ApplyPatch::class)
private class TestConfiguration {
    @Bean
    fun updateSkillById(): UpdateSkillById = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
