package skillmanagement.domain.employees.usecases.read

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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.emptyPage
import skillmanagement.common.model.pageOf
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SearchEmployeesRestAdapter::class)
@Import(SearchEmployeesRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/search", uriPort = 80)
internal class SearchEmployeesRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val getEmployeesPage: GetEmployeesPageFunction
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Employees`() {
        every { getEmployeesPage(any()) } returns emptyPage()

        mockMvc
            .post("/api/employees/_search") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"query": "jane"}"""
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
                              "href": "http://localhost/api/employees/_search?page=0&size=100"
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

        verify { getEmployeesPage(EmployeesMatchingQuery("jane", Pagination.DEFAULT)) }
    }

    @Test
    fun `responds with 200 Ok and Employees if there are any`() {
        every { getEmployeesPage(any()) } returns pageOf(listOf(employee_john_doe, employee_john_smith))

        mockMvc
            .post("/api/employees/_search") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"query": "firstName:john"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_embedded": {
                            "employees": [
                              {
                                "id": "0370f159-2d3b-4e40-9438-10ff34dd62c5",
                                "firstName": "John",
                                "lastName": "Doe",
                                "title": "Hardware Engineer",
                                "email": "john.doe@example.com",
                                "telephone": "+49 123 987654",
                                "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
                                "academicDegrees": [],
                                "certifications": [],
                                "publications": [],
                                "languages": [
                                  {
                                    "language": "English",
                                    "qualifier": "native speaker"
                                  }
                                ],
                                "jobHistory": [
                                  {
                                    "employer": "Example AG",
                                    "title": "Consultant",
                                    "start": "2009-03"
                                  }
                                ],
                                "skills": [
                                  {
                                    "label": "Java",
                                    "level": 7,
                                    "secret": false,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5/skills/f8948935-dab6-4c33-80d0-9f66ae546a7c"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5"
                                      },
                                      "skill": {
                                        "href": "http://localhost/api/employees/f8948935-dab6-4c33-80d0-9f66ae546a7c"
                                      }
                                    }
                                  }
                                ],
                                "projects": [
                                  {
                                    "label": "Orbis",
                                    "description": "The PlayStation 4.",
                                    "contribution": "... sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                                    "startDate": "2009-03-16",
                                    "endDate": "2010-10-31",
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5/projects/b825f016-aa79-4eb8-a896-ae3627efdab7"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5"
                                      },
                                      "project": {
                                        "href": "http://localhost/api/projects/dce233f1-7c20-4250-817e-6676485ddb6e"
                                      }
                                    }
                                  }
                                ],
                                "_links": {
                                  "self": {
                                    "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5"
                                  },
                                  "delete": {
                                    "href": "http://localhost/api/employees/0370f159-2d3b-4e40-9438-10ff34dd62c5"
                                  }
                                }
                              },
                              {
                                "id": "53b5f462-0c39-4e2a-83bf-aa407cf309be",
                                "firstName": "John",
                                "lastName": "Smith",
                                "title": "Junior Software Engineer",
                                "email": "john.smith@example.com",
                                "telephone": "+49 123 948675",
                                "academicDegrees": [],
                                "certifications": [],
                                "publications": [],
                                "languages": [],
                                "jobHistory": [],
                                "skills": [],
                                "projects": [],
                                "_links": {
                                  "self": {
                                    "href": "http://localhost/api/employees/53b5f462-0c39-4e2a-83bf-aa407cf309be"
                                  },
                                  "delete": {
                                    "href": "http://localhost/api/employees/53b5f462-0c39-4e2a-83bf-aa407cf309be"
                                  }
                                }
                              }
                            ]
                          },
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/employees/_search?page=0&size=100"
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

        verify { getEmployeesPage(EmployeesMatchingQuery("firstName:john", Pagination.DEFAULT)) }
    }

    @Test
    fun `responds with 200 Ok and Employees on requested page`() {
        every { getEmployeesPage(any()) } returns pageOf(listOf(employee_jane_doe), 1, 1, 3)

        mockMvc
            .post("/api/employees/_search?page=1&size=1") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"query": "jan*"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_embedded": {
                            "employees": [
                              {
                                "id": "9e1ff73e-0f66-4b86-8548-040d4016bfc9",
                                "firstName": "Jane",
                                "lastName": "Doe",
                                "title": "Senior Software Engineer",
                                "email": "jane.doe@example.com",
                                "telephone": "+49 123 456789",
                                "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
                                "academicDegrees": [
                                  {
                                    "subject": "Computer Science",
                                    "title": "Dr.",
                                    "institution": "MIT"
                                  }
                                ],
                                "certifications": [
                                  "CPSA Advanced Level",
                                  "CPSA Foundation Level"
                                ],
                                "publications": [
                                  "https://github.com/jane_doe11",
                                  "Lorem ipsum dolor (Lorem Magazine, 2021)"
                                ],
                                "languages": [
                                  {
                                    "language": "English",
                                    "qualifier": "native speaker"
                                  },
                                  {
                                    "language": "German",
                                    "qualifier": "B2"
                                  }
                                ],
                                "jobHistory": [
                                  {
                                    "employer": "Example AG",
                                    "title": "Senior Software Engineer",
                                    "start": "2020-01"
                                  },
                                  {
                                    "employer": "Example AG",
                                    "title": "Software Engineer",
                                    "start": "2017-11",
                                    "end": "2019-12"
                                  }
                                ],
                                "skills": [
                                  {
                                    "label": "Kotlin",
                                    "level": 8,
                                    "secret": false,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9/skills/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                      },
                                      "skill": {
                                        "href": "http://localhost/api/employees/3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"
                                      }
                                    }
                                  },
                                  {
                                    "label": "Python",
                                    "level": 4,
                                    "secret": true,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9/skills/6935e550-d041-418a-9070-e37431069232"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                      },
                                      "skill": {
                                        "href": "http://localhost/api/employees/6935e550-d041-418a-9070-e37431069232"
                                      }
                                    }
                                  }
                                ],
                                "projects": [
                                  {
                                    "label": "Neo",
                                    "description": "The PlayStation 4 Pro.",
                                    "contribution": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
                                    "startDate": "2018-01-01",
                                    "endDate": "2020-01-31",
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9/projects/c35af600-4457-4a99-a40d-9570f339d284"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                      },
                                      "project": {
                                        "href": "http://localhost/api/projects/f804d83f-466c-4eab-a58f-4b25ca1778f3"
                                      }
                                    }
                                  },
                                  {
                                    "label": "Morpheus",
                                    "description": "The PlayStation VR Headset.",
                                    "contribution": "Ut enim ad minim veniam ...",
                                    "startDate": "2019-02-01",
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9/projects/bb125fd7-29bc-4661-bf4f-25d7f239801e"
                                      },
                                      "employee": {
                                        "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                      },
                                      "project": {
                                        "href": "http://localhost/api/projects/d5370813-a4cb-42d5-9d28-ce624c718538"
                                      }
                                    }
                                  }
                                ],
                                "_links": {
                                  "self": {
                                    "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                  },
                                  "delete": {
                                    "href": "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9"
                                  }
                                }
                              }
                            ]
                          },
                          "_links": {
                            "self": {
                              "href": "http://localhost/api/employees/_search?page=1&size=1"
                            },
                            "previousPage": {
                              "href": "http://localhost/api/employees/_search?page=0&size=1"
                            },
                            "nextPage": {
                              "href": "http://localhost/api/employees/_search?page=2&size=1"
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

        verify { getEmployeesPage(EmployeesMatchingQuery("jan*", Pagination(PageIndex(1), PageSize(1)))) }
    }

    @Test
    fun `paging data is handed over correctly`() {
        every { getEmployeesPage(any()) } returns emptyPage()

        mockMvc
            .post("/api/employees/_search?page=2&size=42") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"query": "jack"}"""
            }
            .andExpect { status { isOk() } }

        verify { getEmployeesPage(EmployeesMatchingQuery("jack", Pagination(PageIndex(2), PageSize(42)))) }
    }

}

private class SearchEmployeesRestAdapterTestsConfiguration {
    @Bean
    fun findEmployees(): GetEmployeesPageFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-28T12:34:56.789Z")
}
