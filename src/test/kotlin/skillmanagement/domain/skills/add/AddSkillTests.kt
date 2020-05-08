package skillmanagement.domain.skills.add

import io.kotlintest.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.util.IdGenerator
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.test.UnitTest
import skillmanagement.test.sequentialIdGenerator
import skillmanagement.test.uuid

@UnitTest
internal class AddSkillTests {

    val idGenerator: IdGenerator = sequentialIdGenerator("312f3bfc-c9b0-4b4c-9cc4-33242cdfc39e")
    val insertSkillIntoDataStore: InsertSkillIntoDataStore = mockk(relaxUnitFun = true)

    val addSkill = AddSkill(idGenerator, insertSkillIntoDataStore)

    @Test
    fun `correct Skill instance is constructed and stored`() {
        val actualSkill = addSkill(SkillLabel("Kotlin"))

        val expectedSkill = Skill(
            id = uuid("312f3bfc-c9b0-4b4c-9cc4-33242cdfc39e"),
            label = SkillLabel("Kotlin")
        )

        actualSkill shouldBe expectedSkill

        verify { insertSkillIntoDataStore(expectedSkill) }
    }

}
