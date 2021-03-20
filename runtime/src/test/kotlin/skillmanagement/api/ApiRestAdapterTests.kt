package skillmanagement.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.andDocument
import skillmanagement.test.fixedClock
import skillmanagement.test.strictJson
import java.time.Clock

@TechnologyIntegrationTest
@WebMvcTest(ApiRestAdapter::class)
@Import(ApiRestAdapterTestsConfiguration::class)
@AutoConfigureRestDocs("build/generated-snippets/api/get", uriPort = 80)
internal class ApiRestAdapterTests(
    @Autowired private val mockMvc: MockMvc
) {

    @Test
    @WithMockUser
    fun `responds with 200 OK and links to other resources`() {
        mockMvc
            .get("/api")
            .andExpect {
                status { isOk() }
                content {
                    contentType(HAL_JSON)
                    strictJson {
                        """
                        {
                          "_links": {
                            "employees": {
                              "href": "http://localhost/api/employees"
                            },
                            "projects": {
                              "href": "http://localhost/api/projects"
                            },
                            "skills": {
                              "href": "http://localhost/api/skills"
                            },
                            "graphql": {
                              "href": "http://localhost/api/graphql"
                            }
                          }
                        }
                        """
                    }
                }
            }
            .andDocument("ok")
    }

}

private class ApiRestAdapterTestsConfiguration {
    @Bean
    fun clock(): Clock = fixedClock("2021-03-20T12:34:56.789Z")
}
