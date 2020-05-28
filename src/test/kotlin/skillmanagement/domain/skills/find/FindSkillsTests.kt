package skillmanagement.domain.skills.find

import io.kotlintest.matchers.collections.shouldContainInOrder
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.skill_kotlin
import skillmanagement.domain.skills.skill_python
import skillmanagement.test.UnitTest

@UnitTest
internal class FindSkillsTests {

    val findSkillsInDataStore: FindSkillsInDataStore = mockk()
    val findSkills = FindSkills(findSkillsInDataStore)

    @Test
    fun `returns empty list if there are no Skills`() {
        every { findSkillsInDataStore() } returns emptyList()
        findSkills() shouldBe emptyList()
    }

    @Test
    fun `returns all found Skills`() {
        every { findSkillsInDataStore() } returns listOf(skill_kotlin, skill_python)
        findSkills() shouldContainInOrder listOf(skill_kotlin, skill_python)
    }

}
