package skillmanagement.domain.skills.delete

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.test.UnitTest
import skillmanagement.test.uuid

@UnitTest
internal class DeleteSkillByIdTests {

    val deleteSkillFromDataStore: DeleteSkillFromDataStore = mockk(relaxUnitFun = true)
    val deleteSkillById = DeleteSkillById(deleteSkillFromDataStore)

    @Test
    fun `deletes Skill from data store`() {
        val id = uuid()
        deleteSkillById(id)
        verify { deleteSkillFromDataStore(id) }
    }

}
