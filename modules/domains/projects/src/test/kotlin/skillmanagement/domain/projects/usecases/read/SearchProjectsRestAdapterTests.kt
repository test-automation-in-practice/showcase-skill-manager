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
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SearchProjectsRestAdapter::class)
@Import(SearchProjectsRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/search", uriPort = 80)
internal class SearchProjectsRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val getProjectsPage: GetProjectsPageFunction
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Projects`() {
        every { getProjectsPage(any()) } returns emptyPage()

        mockMvc
            .post("/api/projects/_search") {
                contentType = APPLICATION_JSON
                content = """{"query": "neo"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/_search?page=0&size=100"
                            }
                          },
                          "page": {
                            "size": 100,
                            "totalElements": 0,
                            "totalPages": 0,
                            "number": 0
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("empty")

        verify { getProjectsPage(ProjectsMatchingQuery("neo", Pagination.DEFAULT)) }
    }

    @Test
    fun `responds with 200 Ok and Projects if there are any`() {
        every { getProjectsPage(any()) } returns pageOf(listOf(project_neo, project_morpheus))

        mockMvc
            .post("/api/projects/_search") {
                contentType = APPLICATION_JSON
                content = """{"query": "description:playstation"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_embedded": {
                            "projects": [
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
                              },
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
                            ]
                          },
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/_search?page=0&size=100"
                            }
                          },
                          "page": {
                            "size": 100,
                            "totalElements": 2,
                            "totalPages": 1,
                            "number": 0
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("multiple")

        verify { getProjectsPage(ProjectsMatchingQuery("description:playstation", Pagination.DEFAULT)) }
    }

    @Test
    fun `responds with 200 Ok and Projects on requested page`() {
        every { getProjectsPage(any()) } returns pageOf(listOf(project_morpheus), 1, 1, 3)

        mockMvc
            .post("/api/projects/_search?page=1&size=1") {
                contentType = APPLICATION_JSON
                content = """{"query": "description:playstation"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_embedded": {
                            "projects": [
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
                            ]
                          },
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/projects/_search?page=1&size=1"
                            },
                            "previousPage": {
                              "href": "http://localhost/api/projects/_search?page=0&size=1"
                            },
                            "nextPage": {
                              "href": "http://localhost/api/projects/_search?page=2&size=1"
                            }
                          },
                          "page": {
                            "size": 1,
                            "totalElements": 3,
                            "totalPages": 3,
                            "number": 1
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("multiple-pages")

        verify { getProjectsPage(ProjectsMatchingQuery("description:playstation", Pagination(PageIndex(1), PageSize(1)))) }
    }

    @Test
    fun `paging data is handed over correctly`() {
        every { getProjectsPage(any()) } returns emptyPage()

        mockMvc
            .post("/api/projects/_search?page=2&size=42") {
                contentType = APPLICATION_JSON
                content = """{"query": "java"}"""
            }
            .andExpect { status { isOk() } }

        verify { getProjectsPage(ProjectsMatchingQuery("java", Pagination(PageIndex(2), PageSize(42)))) }
    }

}

private class SearchProjectsRestAdapterTestsConfiguration {
    @Bean
    fun findProjects(): GetProjectsPageFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
