package skillmanagement.domain.skills.usecases.read

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.test.UnitTest

@UnitTest
internal class GetSkillByIdFunctionTests {

    val id = skill_kotlin.id

    val getSkillsFromDataStore: GetSkillsFromDataStoreFunction = mockk()
    val getSkillById = GetSkillByIdFunction(getSkillsFromDataStore)

    @Test
    fun `returns NULL if nothing found with given ID`() {
        every { getSkillsFromDataStore(id) } returns null
        getSkillById(id) shouldBe null
    }

    @Test
    fun `returns Skill if found by its ID`() {
        every { getSkillsFromDataStore(id) } returns skill_kotlin
        getSkillById(id) shouldBe skill_kotlin
    }

}
