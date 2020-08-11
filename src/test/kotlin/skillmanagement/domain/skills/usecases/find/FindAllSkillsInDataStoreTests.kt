package skillmanagement.domain.skills.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.add.InsertSkillIntoDataStore
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class FindAllSkillsInDataStoreTests(
    @Autowired val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    val insertSkillIntoDataStore = InsertSkillIntoDataStore(namedParameterJdbcTemplate, objectMapper)
    val findSkills = FindAllSkillsInDataStore(namedParameterJdbcTemplate.jdbcTemplate, objectMapper)

    @Test
    fun `returns empty list if there are no Skills`() {
        findSkills() shouldBe emptyList()
    }

    @Test
    fun `returns single entry list if there is only one Skill`() {
        insertSkillIntoDataStore(skill_kotlin)
        findSkills() shouldContainExactly listOf(skill_kotlin)
    }

    @Test
    fun `returns multi entry list if there are multiple Skills`() {
        insertSkillIntoDataStore(skill_kotlin)
        insertSkillIntoDataStore(skill_python)
        findSkills() shouldContainExactly setOf(skill_kotlin, skill_python)
    }

}
