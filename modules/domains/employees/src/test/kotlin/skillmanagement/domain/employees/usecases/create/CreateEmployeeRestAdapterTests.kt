package skillmanagement.domain.employees.usecases.create

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
import skillmanagement.domain.employees.model.asFreshlyCreatedInstance
import skillmanagement.domain.employees.model.employee_creation_data_jane_doe
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(CreateEmployeeRestAdapter::class)
@Import(CreateEmployeeRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/create", uriPort = 80)
internal class CreateEmployeeRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val createEmployee: CreateEmployeeFunction
) {

    @Test
    fun `well formed request leads to correct response`() {
        every { createEmployee(employee_creation_data_jane_doe) } returns employee_jane_doe.asFreshlyCreatedInstance()

        mockMvc
            .post("/api/employees") {
                contentType = APPLICATION_JSON
                content = """
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "title": "Senior Software Engineer",
                      "email": "jane.doe@example.com",
                      "telephone": "+49 123 456789"
                    }
                    """
            }
            .andExpect {
                status { isCreated() }
                header { string(LOCATION, "http://localhost/api/employees/9e1ff73e-0f66-4b86-8548-040d4016bfc9") }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "id": "9e1ff73e-0f66-4b86-8548-040d4016bfc9",
                          "firstName": "Jane",
                          "lastName": "Doe",
                          "title": "Senior Software Engineer",
                          "email": "jane.doe@example.com",
                          "telephone": "+49 123 456789",
                          "academicDegrees": [],
                          "certifications": [],
                          "publications": [],
                          "languages": [],
                          "jobHistory": [],
                          "skills": [],
                          "projects": [],
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
            .andDocument("created")
    }

    @Test
    fun `malformed request leads to BAD REQUEST response - illegal value`() {
        mockMvc
            .post("/api/employees") {
                contentType = APPLICATION_JSON
                content = """
                    {
                      "firstName": "",
                      "lastName": "Doe",
                      "title": "Senior Software Engineer",
                      "email": "jane.doe@example.com",
                      "telephone": "+49 123 456789"
                    }
                    """
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
                          "path": "/api/employees",
                          "error": "Bad Request",
                          "message": "Request body validation failed!",
                          "details": [
                            "'FirstName' [] - must not be blank!",
                            "'FirstName' [] - must match pattern: (?U)[\\w][\\w -]*"
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
private class CreateEmployeeRestAdapterTestsConfiguration {
    @Bean
    fun createEmployee(): CreateEmployeeFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
