package skillmanagement.domain.employees.usecases.delete

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeByIdResult.SuccessfullyDeleted
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.uuid
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(DeleteEmployeeByIdRestAdapter::class)
@Import(DeleteEmployeeByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/employees/delete-by-id", uriPort = 80)
internal class DeleteEmployeeByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val deleteEmployeeById: DeleteEmployeeByIdFunction
) {

    private val employeeId = uuid("4f1ead02-b667-4f11-bf30-56dae2193be6")

    @Test
    fun `well formed request leads to correct response`() {
        every { deleteEmployeeById(employeeId) } returns SuccessfullyDeleted
        mockMvc
            .delete("/api/employees/$employeeId")
            .andExpect {
                status { isNoContent() }
                content { string("") }
            }
            .andDocument("deleted")

        verify { deleteEmployeeById(employeeId) }
    }

}

private class DeleteEmployeeByIdRestAdapterTestsConfiguration {
    @Bean
    fun deleteEmployeeById(): DeleteEmployeeByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
