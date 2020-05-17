package skillmanagement.domain.skills.add

import io.kotlintest.shouldBe
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.springframework.util.IdGenerator
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.test.UnitTest
import skillmanagement.test.sequentialIdGenerator
import skillmanagement.test.uuid

@UnitTest
internal class AddSkillTests {

    val ids = arrayOf("312f3bfc-c9b0-4b4c-9cc4-33242cdfc39e", "da8748e1-405b-456c-92d4-26fdad09a0c9")
    val idGenerator: IdGenerator = sequentialIdGenerator(*ids)
    val insertSkillIntoDataStore: InsertSkillIntoDataStore = mockk(relaxUnitFun = true)

    val addSkill = AddSkill(idGenerator, insertSkillIntoDataStore)

    @Test
    fun `correct Skill instances are constructed and stored`() {
        val actualSkill1 = addSkill(SkillLabel("Skill #1"))
        val actualSkill2 = addSkill(SkillLabel("Skill #2"))

        val expectedSkill1 = Skill(uuid(ids[0]), SkillLabel("Skill #1"))
        val expectedSkill2 = Skill(uuid(ids[1]), SkillLabel("Skill #2"))

        actualSkill1 shouldBe expectedSkill1
        actualSkill2 shouldBe expectedSkill2

        verifyOrder {
            insertSkillIntoDataStore(expectedSkill1)
            insertSkillIntoDataStore(expectedSkill2)
        }
        confirmVerified(insertSkillIntoDataStore)
    }

}
