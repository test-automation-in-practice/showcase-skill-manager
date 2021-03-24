package skillmanagement.domain.projects.usecases.read

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
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import skillmanagement.test.uuid
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(GetProjectByIdRestAdapter::class)
@Import(GetProjectByIdRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/projects/get-by-id", uriPort = 80)
internal class GetProjectByIdRestAdapterTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val getProjectById: GetProjectByIdFunction
) {

    @Test
    fun `responds with 204 No Content if Project not found`() {
        val id = uuid()
        every { getProjectById(id) } returns null

        mockMvc
            .get("/api/projects/$id")
            .andExpect {
                status { isNoContent() }
                content { string("") }
            }
            .andDocument("not-found")
    }

    @Test
    fun `responds with 200 Ok if Project found`() {
        val id = project_neo.id
        every { getProjectById(id) } returns project_neo

        mockMvc
            .get("/api/projects/$id")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaTypes.HAL_JSON)
                    strictJson {
                        """
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
                        }
                        """
                    }
                }
            }
            .andDocument("found")
    }

}

private class GetProjectByIdRestAdapterTestsConfiguration {
    @Bean
    fun getProjectById(): GetProjectByIdFunction = mockk()

    @Bean
    fun clock(): Clock = fixedClock("2021-03-24T12:34:56.789Z")
}
