package skillmanagement.domain.skills.usecases.get

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
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
import skillmanagement.test.uuid

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetSkillFromDataStoreTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    val insertSkillIntoDataStore = InsertSkillIntoDataStore(jdbcTemplate, objectMapper)
    val getSkill = GetSkillFromDataStore(jdbcTemplate, objectMapper)

    @BeforeAll
    fun insertTestData() {
        insertSkillIntoDataStore(skill_kotlin)
        insertSkillIntoDataStore(skill_python)
    }

    @Nested
    inner class SingleId {

        @Test
        fun `returns NULL if nothing found with given ID`() {
            getSkill(uuid()) shouldBe null
        }

        @Test
        fun `returns Skill if found by its ID`() {
            getSkill(skill_kotlin.id) shouldBe skill_kotlin
        }

    }

    @Nested
    inner class MultipleIds {

        @Test
        fun `returns empty Map for empty ID list`() {
            getSkill(emptyList()) shouldBe emptyMap()
        }

        @Test
        fun `returns empty map if none of the Skills were found`() {
            getSkill(listOf(uuid())) shouldBe emptyMap()
        }

        @Test
        fun `returns map with every found Skill`() {
            getSkill(listOf(skill_kotlin.id, uuid(), skill_python.id)) shouldBe mapOf(
                skill_kotlin.id to skill_kotlin,
                skill_python.id to skill_python
            )
        }

    }

}
