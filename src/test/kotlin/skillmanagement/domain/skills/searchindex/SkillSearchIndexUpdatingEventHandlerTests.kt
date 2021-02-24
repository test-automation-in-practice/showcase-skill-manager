package skillmanagement.domain.skills.searchindex

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.*
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class SkillSearchIndexUpdatingEventHandlerTests {

    private val searchIndex: SkillSearchIndex = mockk(relaxed = true)
    private val cut = SkillSearchIndexUpdatingEventHandler(searchIndex)

    @Test
    fun `SkillAddedEvent will add a new index entry`() {
        cut.handle(SkillAddedEvent(skill_kotlin))
        verify { searchIndex.index(skill_kotlin) }
    }

    @Test
    fun `SkillUpdatedEvent will update an existing index entry`() {
        cut.handle(SkillUpdatedEvent(skill_java))
        verify { searchIndex.index(skill_java) }
    }

    @Test
    fun `SkillDeletedEvent will delete an existing index entry`() {
        cut.handle(SkillDeletedEvent(skill_python))
        verify { searchIndex.deleteById(skill_python.id) }
    }

}
