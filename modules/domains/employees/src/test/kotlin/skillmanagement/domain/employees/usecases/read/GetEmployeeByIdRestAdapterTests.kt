package skillmanagement.domain.employees.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import skillmanagement.test.uuid
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(GetEmployeeByIdRestAdapter::class)
@Import(GetEmployeeByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/get-by-id", uriPort = 80)
internal class GetEmployeeByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val getEmployeeById: GetEmployeeByIdFunction
) {

    @Test
    fun `responds with 204 No Content if Employee not found`() {
        val id = uuid()
        every { getEmployeeById(id) } returns null

        mockMvc
            .get("/api/employees/$id")
            .andExpect {
                status { isNoContent() }
                content { string("") }
            }
            .andDocument("not-found")
    }

    @Test
    fun `responds with 200 Ok if Employee found`() {
        val id = employee_jane_doe.id
        every { getEmployeeById(id) } returns employee_jane_doe

        mockMvc
            .get("/api/employees/$id")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaTypes.HAL_JSON)
                    strictJson {
                        """
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
                        """
                    }
                }
            }
            .andDocument("found")
    }

}

private class GetEmployeeByIdRestAdapterTestsConfiguration {
    @Bean
    fun getEmployeeById(): GetEmployeeByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
