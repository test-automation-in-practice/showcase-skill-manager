package skillmanagement.domain.skills.add

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.get.GetSkillFromDataStore
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.stringOfLength
import skillmanagement.test.uuid

@JdbcTest
@TechnologyIntegrationTest
internal class InsertSkillIntoDataStoreTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate
) {

    val getSkill = GetSkillFromDataStore(jdbcTemplate)
    val insertSkillIntoDataStore = InsertSkillIntoDataStore(jdbcTemplate)

    @Test
    fun `inserts complete Skill data into data store`() {
        val id = uuid()
        val skill = Skill(id, SkillLabel(stringOfLength(10)))

        getSkill(id) shouldBe null
        insertSkillIntoDataStore(skill)
        getSkill(id) shouldBe skill
    }

}
