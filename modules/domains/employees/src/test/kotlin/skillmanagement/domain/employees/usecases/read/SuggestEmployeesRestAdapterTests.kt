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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.employee_suggestion_jane_doe
import skillmanagement.domain.employees.model.employee_suggestion_john_doe
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.strictJson

@ResetMocksAfterEachTest
@TechnologyIntegrationTest
@WebMvcTest(SuggestEmployeesRestAdapter::class)
@Import(SuggestEmployeesRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/suggest", uriPort = 80)
internal class SuggestEmployeesRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val searchIndex: SearchIndex<Employee>
) {

    @Test
    fun `responds with 200 Ok and empty list if there are no matching Employees`() {
        every { searchIndex.suggest(any(), any()) } returns emptyList()

        mockMvc
            .post("/api/employees/_suggest") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"input": "jack"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    strictJson { "[]" }
                }
            }
            .andDocument("empty")

        verify { searchIndex.suggest(input = "jack", max = MaxSuggestions(100)) }
    }

    @Test
    fun `responds with 200 Ok and Employees if there are any`() {
        every { searchIndex.suggest(any(), any()) }
            .returns(listOf(employee_suggestion_jane_doe, employee_suggestion_john_doe))

        mockMvc
            .post("/api/employees/_suggest?max=5") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"input": "doe"}"""
            }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    strictJson {
                        """
                        [
                          {
                            "id": "9e1ff73e-0f66-4b86-8548-040d4016bfc9",
                            "label": "Jane Doe"
                          },
                          {
                            "id": "0370f159-2d3b-4e40-9438-10ff34dd62c5",
                            "label": "John Doe"
                          }
                        ]
                        """
                    }
                }
            }
            .andDocument("multiple")

        verify { searchIndex.suggest(input = "doe", max = MaxSuggestions(5)) }
    }

}

private class SuggestEmployeesRestAdapterTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<Employee> = mockk(relaxUnitFun = true)
}
