package skillmanagement.domain.skills.usecases.update

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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import skillmanagement.common.failure
import skillmanagement.common.http.patch.ApplyPatch
import skillmanagement.common.success
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill_change_data_python_json
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotChanged
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotFound
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(UpdateSkillByIdRestAdapter::class)
@Import(UpdateSkillByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/update", uriPort = 80)
internal class UpdateSkillByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val updateSkillById: UpdateSkillByIdFunction
) {

    @Test
    fun `PUT - when updating a complete skill it's updated state is returned`() {
        every { updateSkillById(skill_kotlin.id, any()) } answers {
            val block: (Skill) -> (Skill) = secondArg()
            success(block(skill_kotlin))
        }

        mockMvc
            .put("/api/skills/${skill_kotlin.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Kotlin (Language)", "description": "description", "tags": ["language"] }"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                          "label": "Kotlin (Language)",
                          "description": "description",
                          "tags": ["language"],
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
    fun `PUT - when update does not change anything the response will be a 200`() {
        every { updateSkillById(skill_python.id, any()) } returns failure(SkillNotChanged(skill_python))

        mockMvc
            .put("/api/skills/${skill_python.id}") {
                contentType = APPLICATION_JSON
                content = skill_change_data_python_json
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "6935e550-d041-418a-9070-e37431069232",
                          "label": "Python",
                          "tags": [],
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/skills/6935e550-d041-418a-9070-e37431069232"
                            },
                            "delete": {
                              "href": "http://localhost/api/skills/6935e550-d041-418a-9070-e37431069232"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("put/not-changed")
    }

    @Test
    fun `PUT - when updating a non-existing skill the response will be a 404`() {
        every { updateSkillById(skill_kotlin.id, any()) } returns failure(SkillNotFound)

        mockMvc
            .put("/api/skills/${skill_kotlin.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Kotlin (Language)", "tags": [] }"""
            }
            .andExpect {
                status { isNotFound() }
                content { string("") }
            }
            .andDocument("put/not-found")
    }

    @Test
    fun `PATCH - JSON Patch can be used to update properties of a skill - label`() {
        every { updateSkillById(skill_kotlin.id, any()) } answers {
            val block: (Skill) -> (Skill) = secondArg()
            success(block(skill_kotlin))
        }

        mockMvc
            .patch("/api/skills/${skill_kotlin.id}") {
                contentType = MediaType("application", "json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Kotlin (Language)"
                      },
                      {
                        "op": "replace",
                        "path": "/description",
                        "value": "description"
                      },
                      {
                        "op": "remove",
                        "path": "/tags/0"
                      },
                      {
                        "op": "add",
                        "path": "/tags/-",
                        "value": "current"
                      }
                    ]
                    """
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                          "label": "Kotlin (Language)",
                          "description": "description",
                          "tags": ["current", "language"],
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
    fun `PATCH - when update does not change anything the response will be a 200`() {
        every { updateSkillById(skill_python.id, any()) } returns failure(SkillNotChanged(skill_python))

        mockMvc
            .patch("/api/skills/${skill_python.id}") {
                contentType = MediaType("application", "json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Python"
                      }
                    ]
                    """
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "6935e550-d041-418a-9070-e37431069232",
                          "label": "Python",
                          "tags": [],
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/skills/6935e550-d041-418a-9070-e37431069232"
                            },
                            "delete": {
                              "href": "http://localhost/api/skills/6935e550-d041-418a-9070-e37431069232"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("patch/not-changed")
    }

    @Test
    fun `PATCH - when updating a non-existing skill the response will be a 404`() {
        every { updateSkillById(skill_kotlin.id, any()) } returns failure(SkillNotFound)

        mockMvc
            .patch("/api/skills/${skill_kotlin.id}") {
                contentType = MediaType("application", "json-patch+json")
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
                status { isNotFound() }
                content { string("") }
            }
            .andDocument("patch/not-found")
    }

}

@Import(ApplyPatch::class)
private class UpdateSkillByIdRestAdapterTestsConfiguration {
    @Bean
    fun updateSkillById(): UpdateSkillByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
