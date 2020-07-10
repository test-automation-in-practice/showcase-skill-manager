package skillmanagement.domain.skills.find

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.add.InsertSkillIntoDataStore
import skillmanagement.domain.skills.skill_kotlin
import skillmanagement.domain.skills.skill_python
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class FindSkillsInDataStoreTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate
) {

    val objectMapper = jacksonObjectMapper()
    val insertSkillIntoDataStore = InsertSkillIntoDataStore(jdbcTemplate, objectMapper)
    val findSkills = FindSkillsInDataStore(jdbcTemplate, objectMapper)

    @Test
    fun `returns empty list if there are no Skills`() {
        findSkills(NoOpQuery) shouldBe emptyList()
    }

    @Test
    fun `returns single entry list if there is only one Skill`() {
        insertSkillIntoDataStore(skill_kotlin)
        findSkills(NoOpQuery) shouldContainExactly listOf(skill_kotlin)
    }

    @Test
    fun `returns multi entry list if there are multiple Skills`() {
        insertSkillIntoDataStore(skill_kotlin)
        insertSkillIntoDataStore(skill_python)
        findSkills(NoOpQuery) shouldContainExactly setOf(skill_kotlin, skill_python)
    }

    @Test
    fun `returns matching skills for label like query`() {
        insertSkillIntoDataStore(skill_kotlin)
        insertSkillIntoDataStore(skill_python)
        findSkills(SkillsWithLabelLike("otl")) shouldContainExactly setOf(skill_kotlin)
        findSkills(SkillsWithLabelLike("Kotlin")) shouldContainExactly setOf(skill_kotlin)
        findSkills(SkillsWithLabelLike("kotlin")) shouldContainExactly setOf(skill_kotlin)
    }

}
