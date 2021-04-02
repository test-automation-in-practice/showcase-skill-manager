package skillmanagement.domain.skills.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.model.skillId
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.usecases.read.GetSkillsFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class InsertSkillIntoDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val getSkill = GetSkillsFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertSkillIntoDataStore = InsertSkillIntoDataStoreFunction(jdbcTemplate, objectMapper)

    @Test
    fun `inserts complete skill data into data store`() {
        val skill = skill_kotlin.copy(id = skillId())

        getSkill(skill.id) shouldBe null
        insertSkillIntoDataStore(skill)
        getSkill(skill.id) shouldBe skill
    }

    @Test
    fun `fails when trying to insert skill with existing ID`() {
        val id = skillId()

        val skill1 = skill_kotlin.copy(id = id)
        val skill2 = skill_java.copy(id = id)

        insertSkillIntoDataStore(skill1)
        assertThrows<DuplicateKeyException> {
            insertSkillIntoDataStore(skill2)
        }
    }

}
