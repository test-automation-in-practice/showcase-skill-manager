package skillmanagement.domain.employees.usecases.update

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
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.employee_change_data_jane_doe_json
import skillmanagement.domain.employees.model.employee_change_data_john_smith_json
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(UpdateEmployeeByIdRestAdapter::class)
@Import(UpdateEmployeeByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/update", uriPort = 80)
internal class UpdateEmployeeByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    @Test
    fun `PUT - when updating a complete employee it's updated state is returned`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } answers {
            val block: (Employee) -> (Employee) = secondArg()
            SuccessfullyUpdated(block(employee_john_smith))
        }

        mockMvc
            .put("/api/employees/${employee_john_smith.id}") {
                contentType = APPLICATION_JSON
                content = employee_change_data_jane_doe_json // from only required to all properties set
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "53b5f462-0c39-4e2a-83bf-aa407cf309be",
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
                        """
                    }
                }
            }
            .andDocument("put/updated")
    }

    @Test
    fun `PUT - when not actually changing anything the response will be a 200 Ok`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } returns EmployeeNotChanged(employee_john_smith)

        mockMvc
            .put("/api/employees/${employee_john_smith.id}") {
                contentType = APPLICATION_JSON
                content = employee_change_data_john_smith_json
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
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
                        """
                    }
                }
            }
    }

    @Test
    fun `PUT - when updating a non-existing employee the response will be a 404`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } returns EmployeeNotFound

        mockMvc
            .put("/api/employees/${employee_john_smith.id}") {
                contentType = APPLICATION_JSON
                content = employee_change_data_john_smith_json
            }
            .andExpect {
                status { isNotFound() }
                content { string("") }
            }
            .andDocument("put/not-found")
    }

    @Test
    fun `PATCH - JSON Patch can be used to update properties of a employee - label`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } answers {
            val block: (Employee) -> (Employee) = secondArg()
            SuccessfullyUpdated(block(employee_john_smith))
        }

        mockMvc
            .patch("/api/employees/${employee_john_smith.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/firstName",
                        "value": "Jonathan"
                      },
                      {
                        "op": "add",
                        "path": "/certifications/-",
                        "value": "CPSA Foundation Level"
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
                          "id": "53b5f462-0c39-4e2a-83bf-aa407cf309be",
                          "firstName": "Jonathan",
                          "lastName": "Smith",
                          "title": "Junior Software Engineer",
                          "email": "john.smith@example.com",
                          "telephone": "+49 123 948675",
                          "academicDegrees": [],
                          "certifications": [
                            "CPSA Foundation Level"
                          ],
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
                        """
                    }
                }
            }
            .andDocument("patch/updated")
    }

    @Test
    fun `PATCH - when not actually changing anything the response will be a 200 Ok`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } answers {
            val block: (Employee) -> (Employee) = secondArg()
            EmployeeNotChanged(block(employee_john_smith))
        }

        mockMvc
            .patch("/api/employees/${employee_john_smith.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/firstName",
                        "value": "John"
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
                        """
                    }
                }
            }
    }

    @Test
    fun `PATCH - when updating a non-existing employee the response will be a 404`() {
        every { updateEmployeeById(employee_john_smith.id, any()) } returns EmployeeNotFound

        mockMvc
            .patch("/api/employees/${employee_john_smith.id}") {
                contentType = MediaType("application","json-patch+json")
                content = """
                    [
                      {
                        "op": "replace",
                        "path": "/firstName",
                        "value": "Jonathan"
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
private class UpdateEmployeeByIdRestAdapterTestsConfiguration {
    @Bean
    fun updateEmployeeById(): UpdateEmployeeByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2020-05-08T12:34:56.789Z")
}
