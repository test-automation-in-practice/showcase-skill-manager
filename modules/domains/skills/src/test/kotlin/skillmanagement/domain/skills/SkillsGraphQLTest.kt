package skillmanagement.domain.skills

import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.mockk.every
import io.mockk.mockk
import mu.KotlinLogging.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus.OK
import skillmanagement.common.searchindices.Page
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.create.CreateSkillFunction
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdFunction
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import skillmanagement.domain.skills.usecases.read.AllSkillsQuery
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.usecases.read.GetSkillsFunction
import skillmanagement.test.ResetMocksAfterEachTest

@GraphQLTest
@ResetMocksAfterEachTest
@Import(TestConfiguration::class)
internal class SkillsGraphQLTest(
    @Autowired private val graphQL: GraphQLTestTemplate
) {

    private val log = logger {}

    @Test
    fun `create skill - max data`(@Autowired createSkill: CreateSkillFunction) {
        every { createSkill(skill_kotlin.label, skill_kotlin.description, skill_kotlin.tags) } returns skill_kotlin
        assertRequestResponse(
            request = "/graphql/createSkill/kotlin.graphql",
            expectedResponseBody = "/graphql/createSkill/kotlin.json"
        )
    }

    @Test
    fun `create skill - min data`(@Autowired createSkill: CreateSkillFunction) {
        every { createSkill(skill_python.label) } returns skill_python
        assertRequestResponse(
            request = "/graphql/createSkill/python.graphql",
            expectedResponseBody = "/graphql/createSkill/python.json"
        )
    }

    @Test
    fun `get skill by id - max data`(@Autowired getSkillById: GetSkillByIdFunction) {
        every { getSkillById(skill_kotlin.id) } returns skill_kotlin
        assertRequestResponse(
            request = "/graphql/getSkillById/kotlin.graphql",
            expectedResponseBody = "/graphql/getSkillById/kotlin.json"
        )
    }

    @Test
    fun `get skill by id - min data`(@Autowired getSkillById: GetSkillByIdFunction) {
        every { getSkillById(skill_python.id) } returns skill_python
        assertRequestResponse(
            request = "/graphql/getSkillById/python.graphql",
            expectedResponseBody = "/graphql/getSkillById/python.json"
        )
    }

    @Test
    fun `get skills page - found`(@Autowired getSkills: GetSkillsFunction) {
        every { getSkills(AllSkillsQuery(PageIndex(0), PageSize(10))) }
            .returns(Page(listOf(skill_kotlin, skill_python), 0, 10, 2))
        assertRequestResponse(
            request = "/graphql/getSkillsPage/first-page.graphql",
            expectedResponseBody = "/graphql/getSkillsPage/found.json"
        )
    }

    @Test
    fun `get skills page - empty`(@Autowired getSkills: GetSkillsFunction) {
        every { getSkills(AllSkillsQuery(PageIndex(1), PageSize(10))) }
            .returns(Page(emptyList(), 1, 10, 2))
        assertRequestResponse(
            request = "/graphql/getSkillsPage/second-page.graphql",
            expectedResponseBody = "/graphql/getSkillsPage/empty.json"
        )
    }

    @Test
    fun `delete skill by ID - deleted`(@Autowired deleteSkillById: DeleteSkillByIdFunction) {
        every { deleteSkillById(skill_kotlin.id) } returns SuccessfullyDeleted
        assertRequestResponse(
            request = "/graphql/deleteSkillById/kotlin.graphql",
            expectedResponseBody = "/graphql/deleteSkillById/deleted.json"
        )
    }

    @Test
    fun `delete skill by ID - not found`(@Autowired deleteSkillById: DeleteSkillByIdFunction) {
        every { deleteSkillById(skill_kotlin.id) } returns SkillNotFound
        assertRequestResponse(
            request = "/graphql/deleteSkillById/kotlin.graphql",
            expectedResponseBody = "/graphql/deleteSkillById/not-found.json"
        )
    }

    private fun assertRequestResponse(request: String, expectedResponseBody: String) {
        val response = graphQL.postForResource(request)
        log.info { response.rawResponse }
        assertThat(response.statusCode).isEqualTo(OK)
        response.assertThatJsonContent()
            .isStrictlyEqualToJson(ClassPathResource(expectedResponseBody))
    }

}

private class TestConfiguration {

    @Bean
    fun createSkillFunction(): CreateSkillFunction = mockk()

    @Bean
    fun deleteSkillByIdFunction(): DeleteSkillByIdFunction = mockk()

    @Bean
    fun getSkillByIdFunction(): GetSkillByIdFunction = mockk()

    @Bean
    fun getSkillsFunction(): GetSkillsFunction = mockk()

}
