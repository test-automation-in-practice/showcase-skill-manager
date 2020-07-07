package skillmanagement.domain.skills.delete

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.add.InsertSkillIntoDataStore
import skillmanagement.domain.skills.get.GetSkillFromDataStore
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.stringOfLength
import skillmanagement.test.uuid

@JdbcTest
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class DeleteSkillFromDataStoreTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate
) {

    val objectMapper = jacksonObjectMapper()
    val getSkill = GetSkillFromDataStore(jdbcTemplate, objectMapper)
    val insertSkillIntoDataStore = InsertSkillIntoDataStore(jdbcTemplate, objectMapper)

    val deleteSkillFromDataStore = DeleteSkillFromDataStore(jdbcTemplate)

    @Test
    fun `does not fail if skill with id does not exist`() {
        val id = uuid()
        getSkill(id) shouldBe null
        deleteSkillFromDataStore(id)
    }

    @Test
    fun `deletes existing skill from data store`() {
        val id = uuid()
        val skill = Skill(id, SkillLabel(stringOfLength(10)))

        getSkill(id) shouldBe null
        insertSkillIntoDataStore(skill)
        getSkill(id) shouldBe skill
        deleteSkillFromDataStore(id)
        getSkill(id) shouldBe null
    }

}
