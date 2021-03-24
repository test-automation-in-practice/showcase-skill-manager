package skillmanagement.domain.projects.usecases.delete

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
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.uuid
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(DeleteProjectByIdRestAdapter::class)
@Import(DeleteProjectByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/delete-by-id", uriPort = 80)
internal class DeleteProjectByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val deleteProjectById: DeleteProjectByIdFunction
) {

    private val projectId = uuid("14ae4e75-5cf6-4b30-9fc2-7037bd428584")

    @Test
    fun `well formed request leads to correct response`() {
        every { deleteProjectById(projectId) } returns SuccessfullyDeleted
        mockMvc
            .delete("/api/projects/$projectId")
            .andExpect {
                status { isNoContent() }
                content { string("") }
            }
            .andDocument("deleted")

        verify { deleteProjectById(projectId) }
    }

}

private class DeleteProjectByIdRestAdapterTestsConfiguration {
    @Bean
    fun deleteProjectById(): DeleteProjectByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
