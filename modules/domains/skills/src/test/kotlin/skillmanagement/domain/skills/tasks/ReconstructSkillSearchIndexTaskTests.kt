package skillmanagement.domain.skills.tasks

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.read.GetSkillsFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructSkillSearchIndexTaskTests {

    private val searchIndex: SearchIndexAdmin<Skill> = mockk(relaxUnitFun = true)
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction = mockk()

    private val cut = ReconstructSkillSearchIndexTask(searchIndex, getSkillsFromDataStore)

    @Test
    fun `if data store is empty the index is reset but nothing will be indexed`() {
        stubDataStoreToBeEmpty()

        cut.run()

        verify { searchIndex.reset() }
        verify(exactly = 0) { searchIndex.index(any()) }
    }

    @Test
    fun `every found skill is indexed`() {
        stubDataStoreToContain(skill_kotlin, skill_java, skill_python)

        cut.run()

        verifyOrder {
            searchIndex.reset()
            searchIndex.index(skill_kotlin)
            searchIndex.index(skill_java)
            searchIndex.index(skill_python)
        }
    }

    @Test
    @RecordLoggers(ReconstructSkillSearchIndexTask::class)
    fun `task steps are logged`(log: LogRecord) {
        stubDataStoreToContain(skill_kotlin, skill_java)

        cut.run()

        with(log.messages) {
            assertThat(get(0)).isEqualTo("Reconstructing '$searchIndex':")
            assertThat(get(1)).isEqualTo("Resetting '$searchIndex':")
            assertThat(get(2)).containsSubsequence("Reset of '$searchIndex' finished. Took ", "ms.")
            assertThat(get(3)).isEqualTo("Repopulating '$searchIndex':")
            assertThat(get(4)).isEqualTo("Indexing [3f7985b9-f5f0-4662-bda9-1dcde01f5f3b - Kotlin]")
            assertThat(get(5)).isEqualTo("Indexing [f8948935-dab6-4c33-80d0-9f66ae546a7c - Java]")
            assertThat(get(6)).containsSubsequence("Repopulation of '$searchIndex' finished. Took ", "ms.")
            assertThat(get(7)).containsSubsequence("Reconstruction of '$searchIndex' finished. Took ", "ms.")
        }
    }

    private fun stubDataStoreToBeEmpty() = stubDataStoreToContain()

    private fun stubDataStoreToContain(vararg skills: Skill) {
        every { getSkillsFromDataStore(any<(Skill) -> Unit>()) } answers {
            val consumer = firstArg<(Skill) -> Unit>()
            skills.forEach { consumer(it) }
        }
    }

}
