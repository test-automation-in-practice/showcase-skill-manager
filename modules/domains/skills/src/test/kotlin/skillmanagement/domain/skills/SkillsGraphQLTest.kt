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
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.skill_creation_data_kotlin
import skillmanagement.domain.skills.model.skill_creation_data_python
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.model.skill_suggestion_kotlin
import skillmanagement.domain.skills.usecases.create.CreateSkillFunction
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdFunction
import skillmanagement.domain.skills.usecases.read.AllSkillsQuery
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.usecases.read.GetSkillsPageFunction
import skillmanagement.domain.skills.usecases.read.SkillsMatchingQuery
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
        every { createSkill(skill_creation_data_kotlin) } returns skill_kotlin
        assertRequestResponse(
            request = "/graphql/createSkill/kotlin.graphql",
            expectedResponseBody = "/graphql/createSkill/kotlin.json"
        )
    }

    @Test
    fun `create skill - min data`(@Autowired createSkill: CreateSkillFunction) {
        every { createSkill(skill_creation_data_python) } returns skill_python
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
    fun `get skill by id - not found`(@Autowired getSkillById: GetSkillByIdFunction) {
        every { getSkillById(skill_java.id) } returns null
        assertRequestResponse(
            request = "/graphql/getSkillById/java.graphql",
            expectedResponseBody = "/graphql/getSkillById/not-found.json"
        )
    }

    @Test
    fun `get skills page - found`(@Autowired getSkillsPage: GetSkillsPageFunction) {
        every { getSkillsPage(AllSkillsQuery(Pagination(PageIndex(0), PageSize(10)))) }
            .returns(Page(listOf(skill_kotlin, skill_python), 0, 10, 2))
        assertRequestResponse(
            request = "/graphql/getSkillsPage/first-page.graphql",
            expectedResponseBody = "/graphql/getSkillsPage/found.json"
        )
    }

    @Test
    fun `get skills page - empty`(@Autowired getSkillsPage: GetSkillsPageFunction) {
        every { getSkillsPage(AllSkillsQuery(Pagination(PageIndex(1), PageSize(10)))) }
            .returns(Page(emptyList(), 1, 10, 2))
        assertRequestResponse(
            request = "/graphql/getSkillsPage/second-page.graphql",
            expectedResponseBody = "/graphql/getSkillsPage/empty.json"
        )
    }

    @Test
    fun `search skills - found`(@Autowired getSkillsPage: GetSkillsPageFunction) {
        every { getSkillsPage(SkillsMatchingQuery("tags:language", Pagination(PageIndex(0), PageSize(10)))) }
            .returns(Page(listOf(skill_kotlin, skill_python), 0, 10, 2))
        assertRequestResponse(
            request = "/graphql/searchSkills/first-page.graphql",
            expectedResponseBody = "/graphql/searchSkills/found.json"
        )
    }

    @Test
    fun `search skills - empty`(@Autowired getSkillsPage: GetSkillsPageFunction) {
        every { getSkillsPage(SkillsMatchingQuery("tags:language", Pagination(PageIndex(1), PageSize(10)))) }
            .returns(Page(emptyList(), 1, 10, 2))
        assertRequestResponse(
            request = "/graphql/searchSkills/second-page.graphql",
            expectedResponseBody = "/graphql/searchSkills/empty.json"
        )
    }

    @Test
    fun `suggest skills - found`(@Autowired searchIndex: SearchIndex<Skill, SkillId>) {
        every { searchIndex.suggest("ko", MaxSuggestions(10)) } returns listOf(skill_suggestion_kotlin)
        assertRequestResponse(
            request = "/graphql/suggestSkills/request.graphql",
            expectedResponseBody = "/graphql/suggestSkills/found.json"
        )
    }

    @Test
    fun `suggest skills - empty`(@Autowired searchIndex: SearchIndex<Skill, SkillId>) {
        every { searchIndex.suggest("ko", MaxSuggestions(10)) } returns emptyList()
        assertRequestResponse(
            request = "/graphql/suggestSkills/request.graphql",
            expectedResponseBody = "/graphql/suggestSkills/empty.json"
        )
    }

    @Test
    fun `delete skill by ID - deleted`(@Autowired deleteSkillById: DeleteSkillByIdFunction) {
        every { deleteSkillById(skill_kotlin.id) } returns true
        assertRequestResponse(
            request = "/graphql/deleteSkillById/kotlin.graphql",
            expectedResponseBody = "/graphql/deleteSkillById/deleted.json"
        )
    }

    @Test
    fun `delete skill by ID - not found`(@Autowired deleteSkillById: DeleteSkillByIdFunction) {
        every { deleteSkillById(skill_kotlin.id) } returns false
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
    fun getSkillsFunction(): GetSkillsPageFunction = mockk()

    @Bean
    fun searchIndex(): SearchIndex<Skill, SkillId> = mockk()

}
