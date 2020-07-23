package skillmanagement

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import skillmanagement.domain.employees.EmailAddress
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.TelephoneNumber
import skillmanagement.domain.employees.Title
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.test.End2EndTest
import kotlin.reflect.KClass

@End2EndTest
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AcceptanceTests(
    @Autowired val mockMvc: MockMvc
) {

    val objectMapper = ObjectMapper()
        .apply { registerModule(KotlinModule()) }
        .apply { registerModule(JavaTimeModule()) }
        .apply { registerModule(Jackson2HalModule()) }

    @Nested
    @WithMockUser
    inner class Skills {

        @Test
        fun `add, get and delete skill`() {
            val skill = mockMvc
                .post("/api/skills") {
                    contentType = APPLICATION_JSON
                    content = """{ "label": "Kotlin" }"""
                }
                .andExpect {
                    status { isCreated }
                    content { contentType(HAL_JSON) }
                }
                .extract(SkillResource::class)
                .also {
                    assertThat(it.id).isNotNull()
                    assertThat(it.label).isEqualTo(SkillLabel("Kotlin"))
                    assertThat(it.getLink("self")).isPresent()
                }
            val skillPath = skill.selfLinkPath()

            mockMvc
                .get(skillPath)
                .andExpect {
                    status { isOk }
                    content { contentType(HAL_JSON) }
                }
                .extract(SkillResource::class)
                .also { assertThat(it).isEqualTo(skill) }

            mockMvc
                .delete(skillPath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }

            mockMvc
                .get(skillPath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }
        }

    }

    @Nested
    @WithMockUser
    inner class Projects {

        @Test
        fun `add, get and delete project`() {
            val project = mockMvc
                .post("/api/projects") {
                    contentType = APPLICATION_JSON
                    content = """
                        {
                          "label": "Testautomation Consulting at Example AG",
                          "description": "Lorem Ipsum ..."
                        }
                        """
                }
                .andExpect {
                    status { isCreated }
                    content { contentType(HAL_JSON) }
                }
                .extract(ProjectResource::class)
                .also {
                    assertThat(it.id).isNotNull()
                    assertThat(it.label).isEqualTo(ProjectLabel("Testautomation Consulting at Example AG"))
                    assertThat(it.description).isEqualTo(ProjectDescription("Lorem Ipsum ..."))
                    assertThat(it.getLink("self")).isPresent()
                }
            val projectPath = project.selfLinkPath()

            mockMvc
                .get(projectPath)
                .andExpect {
                    status { isOk }
                    content { contentType(HAL_JSON) }
                }
                .extract(ProjectResource::class)
                .also { assertThat(it).isEqualTo(project) }

            mockMvc
                .delete(projectPath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }

            mockMvc
                .get(projectPath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }
        }

    }

    @Nested
    @WithMockUser
    inner class Employees {

        @Test
        fun `add, get and delete employee`() {
            val employee = mockMvc
                .post("/api/employees") {
                    contentType = APPLICATION_JSON
                    content = """
                        {
                          "firstName": "Max",
                          "lastName": "Mustermann",
                          "title": "Managing Consultant",
                          "email": "max.musterman@beispiel-gmbh.de",
                          "telephone": "+49 (555) 123456"
                        }
                        """
                }
                .andExpect {
                    status { isCreated }
                    content { contentType(HAL_JSON) }
                }
                .extract(EmployeeResource::class)
                .also {
                    assertThat(it.id).isNotNull()
                    assertThat(it.firstName).isEqualTo(FirstName("Max"))
                    assertThat(it.lastName).isEqualTo(LastName("Mustermann"))
                    assertThat(it.title).isEqualTo(Title("Managing Consultant"))
                    assertThat(it.email).isEqualTo(EmailAddress("max.musterman@beispiel-gmbh.de"))
                    assertThat(it.telephone).isEqualTo(TelephoneNumber("+49 (555) 123456"))
                    assertThat(it.lastUpdate).isNotNull()
                    assertThat(it.getLink("self")).isPresent()
                }
            val employeePath = employee.selfLinkPath()

            mockMvc
                .get(employeePath)
                .andExpect {
                    status { isOk }
                    content { contentType(HAL_JSON) }
                }
                .extract(EmployeeResource::class)
                .also { assertThat(it).isEqualTo(employee) }

            mockMvc
                .delete(employeePath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }

            mockMvc
                .get(employeePath)
                .andExpect {
                    status { isNoContent }
                    content { string("") }
                }
        }

    }

    private fun <T : Any> ResultActionsDsl.extract(clazz: KClass<T>): T {
        val mvcResult = andReturn()
        return objectMapper.readValue(mvcResult.response.contentAsByteArray, clazz.java)
    }

    private fun RepresentationModel<*>.selfLinkPath(): String = linkPath("self")
    private fun RepresentationModel<*>.linkPath(relation: String): String = getRequiredLink(relation).toUri().path

}
