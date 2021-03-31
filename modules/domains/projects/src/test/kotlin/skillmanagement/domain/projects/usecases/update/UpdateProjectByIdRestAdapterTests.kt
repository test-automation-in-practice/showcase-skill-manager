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
import skillmanagement.common.failure
import skillmanagement.common.http.patch.ApplyPatch
import skillmanagement.common.success
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.project_change_data_morpheus_json
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotChanged
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotFound
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
            success(block(project))
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
    fun `PUT - when update does not change anything the response will be a 200`() {
        every { updateProjectById(project_morpheus.id, any()) } returns failure(ProjectNotChanged(project_morpheus))

        mockMvc
            .put("/api/projects/${project_morpheus.id}") {
                contentType = APPLICATION_JSON
                content = project_change_data_morpheus_json
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
                          "label": "Morpheus",
                          "description": "The PlayStation VR Headset.",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/d5370813-a4cb-42d5-9d28-ce624c718538"
                            },
                            "delete": {
                              "href": "http://localhost/api/projects/d5370813-a4cb-42d5-9d28-ce624c718538"
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
    fun `PUT - when updating a non-existing project the response will be a 404`() {
        every { updateProjectById(project.id, any()) } returns failure(ProjectNotFound)

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
            success(block(project))
        }

        mockMvc
            .patch("/api/projects/${project.id}") {
                contentType = MediaType("application", "json-patch+json")
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
    fun `PATCH - when update does not change anything the response will be a 200`() {
        every { updateProjectById(project_morpheus.id, any()) } returns failure(ProjectNotChanged(project_morpheus))

        mockMvc
            .patch("/api/projects/${project_morpheus.id}") {
                contentType = MediaType("application", "json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/label",
                        "value": "Morpheus"
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
                          "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
                          "label": "Morpheus",
                          "description": "The PlayStation VR Headset.",
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/d5370813-a4cb-42d5-9d28-ce624c718538"
                            },
                            "delete": {
                              "href": "http://localhost/api/projects/d5370813-a4cb-42d5-9d28-ce624c718538"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("patch/not-change")
    }

    @Test
    fun `PATCH - when updating a non-existing project the response will be a 404`() {
        every { updateProjectById(project.id, any()) } returns failure(ProjectNotFound)

        mockMvc
            .patch("/api/projects/${project.id}") {
                contentType = MediaType("application", "json-patch+json")
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
