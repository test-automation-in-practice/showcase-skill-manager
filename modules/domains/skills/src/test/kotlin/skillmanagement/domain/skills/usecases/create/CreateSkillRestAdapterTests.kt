package skillmanagement.domain.skills.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.common.http.error.GlobalRestControllerAdvice
import skillmanagement.domain.skills.model.skill_creation_data_kotlin
import skillmanagement.domain.skills.model.skill_creation_data_python
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(CreateSkillRestAdapter::class)
@Import(CreateSkillRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/skills/create", uriPort = 80)
internal class CreateSkillRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val createSkill: CreateSkillFunction
) {

    @Test
    fun `well formed request leads to correct response`() {
        every { createSkill(skill_creation_data_kotlin) } returns skill_kotlin

        mockMvc
            .post("/api/skills") {
                contentType = APPLICATION_JSON
                content = """
                    { 
                      "label": "Kotlin",
                      "description": "The coolest programming language.",
                      "tags": ["language", "cool"]
                    }
                    """
            }
            .andExpect {
                status { isCreated() }
                header { string(LOCATION, "http://localhost/api/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b") }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
                          "label": "Kotlin",
                          "description": "The coolest programming language.",
                          "tags": ["cool", "language"],
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
            .andDocument("created")
    }

    @Test
    fun `tags are optional`() {
        every { createSkill(skill_creation_data_python) } returns skill_python

        mockMvc
            .post("/api/skills") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Python" }"""
            }
            .andExpect {
                status { isCreated() }
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
    }

    @Test
    fun `malformed request leads to BAD REQUEST response - illegal value`() {
        mockMvc
            .post("/api/skills") {
                contentType = APPLICATION_JSON
                content = """{ "label": "" }"""
            }
            .andExpect {
                status { isBadRequest() }
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
                            "'SkillLabel' [] - must not be blank!"
                          ]
                        }
                        """
                    }
                }
            }
            .andDocument("bad-request")
    }

}

@Import(GlobalRestControllerAdvice::class)
private class CreateSkillRestAdapterTestsConfiguration {
    @Bean
    fun createSkill(): CreateSkillFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
