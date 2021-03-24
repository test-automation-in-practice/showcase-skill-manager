package skillmanagement.domain.projects.usecases.update

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
import skillmanagement.common.http.patch.ApplyPatch
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.update.UpdateProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.update.UpdateProjectByIdResult.SuccessfullyUpdated
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(UpdateProjectByIdRestAdapter::class)
@Import(UpdateProjectByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/update", uriPort = 80)
internal class UpdateProjectByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val updateProjectById: UpdateProjectByIdFunction
) {

    private val project = project_neo

    @Test
    fun `PUT - when updating a complete project it's updated state is returned`() {
        every { updateProjectById(project.id, any()) } answers {
            val block: (Project) -> (Project) = secondArg()
            SuccessfullyUpdated(block(project))
        }

        mockMvc
            .put("/api/projects/${project.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Project Neo", "description": "description" }"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
                          "label": "Project Neo",
                          "description": "description",
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
            .andDocument("put/updated")
    }

    @Test
    fun `PUT - when updating a non-existing project the response will be a 404`() {
        every { updateProjectById(project.id, any()) } returns ProjectNotFound

        mockMvc
            .put("/api/projects/${project.id}") {
                contentType = APPLICATION_JSON
                content = """{ "label": "Project Neo", "description": "description" }"""
            }
            .andExpect {
                status { isNotFound() }
                content { string("") }
            }
            .andDocument("put/not-found")
    }

    @Test
    fun `PATCH - JSON Patch can be used to update properties of a project - label`() {
        every { updateProjectById(project.id, any()) } answers {
            val block: (Project) -> (Project) = secondArg()
            SuccessfullyUpdated(block(project))
        }

        mockMvc
            .patch("/api/projects/${project.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Project Neo"
                      },
                      {
                        "op": "replace",
                        "path": "/description",
                        "value": "description"
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
                          "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
                          "label": "Project Neo",
                          "description": "description",
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
            .andDocument("patch/updated")
    }

    @Test
    fun `PATCH - when updating a non-existing project the response will be a 404`() {
        every { updateProjectById(project.id, any()) } returns ProjectNotFound

        mockMvc
            .patch("/api/projects/${project.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Project Neo"
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
private class UpdateProjectByIdRestAdapterTestsConfiguration {
    @Bean
    fun updateProjectById(): UpdateProjectByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
