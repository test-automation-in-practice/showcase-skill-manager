package skillmanagement.domain.skills.usecases.delete

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.usecases.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteSkillByIdFunctionTests {

    private val getSkillById: GetSkillByIdFunction = mockk()
    private val deleteSkillFromDataStore: DeleteSkillFromDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val deleteSkillById = DeleteSkillByIdFunction(getSkillById, deleteSkillFromDataStore, publishEvent)

    @Test
    fun `given skill with ID does not exist when deleting by ID then the result will be SkillNotFound`() {
        every { getSkillById(skill_kotlin.id) } returns null

        deleteSkillById(skill_kotlin.id) shouldBe SkillNotFound

        verify { deleteSkillFromDataStore wasNot called }
        verify { publishEvent wasNot called }
    }

    @Test
    fun `given skill with ID exists when deleting by ID then the result will be SuccessfullyDeleted`() {
        every { getSkillById(skill_kotlin.id) } returns skill_kotlin

        deleteSkillById(skill_kotlin.id) shouldBe SuccessfullyDeleted

        verify { deleteSkillFromDataStore(skill_kotlin.id) }
        verify { publishEvent(SkillDeletedEvent(skill_kotlin)) }
    }

}
