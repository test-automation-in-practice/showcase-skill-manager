package skillmanagement.domain.skills.usecases.read

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsInOrder
import io.github.logrecorder.logback.junit5.RecordLoggers
import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.skillId
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.create.InsertSkillIntoDataStoreFunction
import skillmanagement.domain.skills.usecases.delete.DeleteSkillFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetSkillsFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val insertSkillIntoDataStore = InsertSkillIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val deleteSkillFromDataStore = DeleteSkillFromDataStoreFunction(jdbcTemplate)
    private val getSkillsFromDataStore = GetSkillsFromDataStoreFunction(jdbcTemplate, objectMapper)

    @AfterEach
    fun deleteSkills() {
        deleteSkillFromDataStore()
    }

    @Nested
    inner class SingleId {

        @Test
        fun `returns NULL if nothing found with given ID`() {
            getSingleSkill(skillId()) shouldBe null
        }

        @Test
        fun `returns Skill if found by its ID`() {
            insert(skill_kotlin)
            getSingleSkill(skill_kotlin.id) shouldBe skill_kotlin
        }

        private fun getSingleSkill(id: SkillId) = getSkillsFromDataStore(id)

    }

    @Nested
    inner class MultipleIds {

        @Test
        fun `returns empty Map for empty ID list`() {
            getMultipleSkills() shouldBe emptyMap()
        }

        @Test
        fun `returns empty map if none of the Skills were found`() {
            getMultipleSkills(skillId()) shouldBe emptyMap()
        }

        @Test
        fun `returns map with every found Skill`() {
            insert(skill_kotlin, skill_python)

            val actualSkills = getMultipleSkills(skill_kotlin.id, skillId(), skill_python.id)
            val expectedSkills = setOf(skill_kotlin, skill_python).map { it.id to it }.toMap()

            actualSkills shouldBe expectedSkills
        }

        private fun getMultipleSkills(vararg ids: SkillId) = getSkillsFromDataStore(ids.toList(), chunkSize = 2)

    }

    @Nested
    @ResetMocksAfterEachTest
    inner class AllWithCallback {

        private val callback: (SkillEntity) -> Unit = mockk(relaxed = true)

        @Test
        fun `callback is never invoked if there are no skills`() {
            execute()
            verify { callback wasNot called }
        }

        @Test
        fun `callback is invoked for each existing skill`() {
            insert(skill_kotlin, skill_python)

            execute()

            verify {
                callback(skill_kotlin)
                callback(skill_python)
            }
            confirmVerified(callback)
        }

        private fun execute() = getSkillsFromDataStore(callback)

    }

    @Nested
    inner class Deserialization {

        private val skill = skill_kotlin
        private val skillId = skill.id

        @Test
        @RecordLoggers(SkillRowMapper::class)
        fun `deserialization errors are logged but don't throw an exception`(log: LogRecord) {
            insert(skill)

            assertThat(getSkillsFromDataStore(skillId)).isNotNull()
            corruptData(skillId)
            assertThat(getSkillsFromDataStore(skillId)).isNull()

            assertThat(log) containsInOrder {
                error(startsWith("Could not read data of skill [$skillId]: Instantiation of"))
                debug(startsWith("Corrupted data: {}"))
            }
        }

        private fun corruptData(skillId: SkillId) {
            jdbcTemplate.update("UPDATE skills SET data = '{}' WHERE id = :id", mapOf("id" to "$skillId"))
        }

    }

    private fun insert(vararg skills: SkillEntity) = skills.forEach { insertSkillIntoDataStore(it) }

}
