package skillmanagement.domain.skills.get

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.skill_kotlin
import skillmanagement.test.UnitTest

@UnitTest
internal class GetSkillByIdTests {

    val id = skill_kotlin.id

    val getSkillFromDataStore: GetSkillFromDataStore = mockk()
    val getSkillById = GetSkillById(getSkillFromDataStore)

    @Test
    fun `returns NULL if nothing found with given ID`() {
        every { getSkillFromDataStore(id) } returns null
        getSkillById(id) shouldBe null
    }

    @Test
    fun `returns Skill if found by its ID`() {
        every { getSkillFromDataStore(id) } returns skill_kotlin
        getSkillById(id) shouldBe skill_kotlin
    }

}
