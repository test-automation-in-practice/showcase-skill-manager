package skillmanagement.domain.skills.delete

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.domain.PublishEvent
import skillmanagement.domain.skills.SkillDeletedEvent
import skillmanagement.domain.skills.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import skillmanagement.domain.skills.get.GetSkillById
import skillmanagement.domain.skills.skill_kotlin
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteSkillByIdTests {

    val getSkillById: GetSkillById = mockk()
    val deleteSkillFromDataStore: DeleteSkillFromDataStore = mockk(relaxUnitFun = true)
    val publishEvent: PublishEvent = mockk(relaxUnitFun = true)
    val deleteSkillById = DeleteSkillById(getSkillById, deleteSkillFromDataStore, publishEvent)

    val skill = skill_kotlin
    val skillId = skill.id

    @Test
    fun `given skill with ID does not exist when deleting by ID then the result will be SkillNotFound`() {
        every { getSkillById(skillId) } returns null

        deleteSkillById(skillId) shouldBe SkillNotFound

        verify { deleteSkillFromDataStore wasNot called }
        verify { publishEvent wasNot called }
    }

    @Test
    fun `given skill with ID exists when deleting by ID then the result will be SuccessfullyDeleted`() {
        every { getSkillById(skillId) } returns skill

        deleteSkillById(skillId) shouldBe SuccessfullyDeleted

        verify { deleteSkillFromDataStore(skillId) }
        verify { publishEvent(SkillDeletedEvent(skill)) }
    }

}
