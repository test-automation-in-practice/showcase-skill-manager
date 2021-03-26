package skillmanagement.domain.projects.usecases.create

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
import skillmanagement.domain.projects.model.asFreshlyCreatedInstance
import skillmanagement.domain.projects.model.project_creation_data_neo
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(CreateProjectRestAdapter::class)
@Import(CreateProjectRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/create", uriPort = 80)
internal class CreateProjectRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val createProject: CreateProjectFunction
) {

    @Test
    fun `well formed request leads to correct response`() {
        every { createProject(project_creation_data_neo) } returns project_neo.asFreshlyCreatedInstance()

        mockMvc
            .post("/api/projects") {
                contentType = APPLICATION_JSON
                content = """
                    {
                      "label": "Neo",
                      "description": "The PlayStation 4 Pro."
                    }
                    """
            }
            .andExpect {
                status { isCreated() }
                header { string(LOCATION, "http://localhost/api/projects/f804d83f-466c-4eab-a58f-4b25ca1778f3") }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
                          "label": "Neo",
                          "description": "The PlayStation 4 Pro.",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/f804d83f-466c-4eab-a58f-4b25ca1778f3"
                            },
                            "delete": {
                              "href": "http://localhost/api/projects/f804d83f-466c-4eab-a58f-4b25ca1778f3"
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
            .post("/api/projects") {
                contentType = APPLICATION_JSON
                content = """{ "label": "", "description": "Lorem Ipsum" }"""
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    contentType(APPLICATION_JSON)
                    strictJson {
                        """
                        {
                          "timestamp": "2021-03-24T12:34:56.789Z",
                          "status": 400,
                          "path": "/api/projects",
                          "error": "Bad Request",
                          "message": "Request body validation failed!",
                          "details": [
                            "'ProjectLabel' [] - must not be blank!"
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
private class CreateProjectRestAdapterTestsConfiguration {
    @Bean
    fun createProject(): CreateProjectFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
