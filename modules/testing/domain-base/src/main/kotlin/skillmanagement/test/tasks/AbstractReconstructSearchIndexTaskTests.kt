package skillmanagement.test.tasks

import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.searchindices.SearchIndexAdmin

abstract class AbstractReconstructSearchIndexTaskTests<T : Any> {

    abstract val searchIndexAdmin: SearchIndexAdmin<T>
    abstract val cut: AbstractReconstructSearchIndexTask<T>

    abstract val instance1: T
    abstract val instance2: T
    abstract val instance3: T

    abstract fun stubDataStoreToBeEmpty()
    abstract fun stubDataStoreToContain(instances: Collection<T>)
    abstract fun expectedShortDescription(instance: T): String

    @Test
    fun `if data store is empty the index is reset but nothing will be indexed`() {
        stubDataStoreToBeEmpty()

        cut.run()

        verify(atLeast = 1) { searchIndexAdmin.toString() }
        verify { searchIndexAdmin.reset() }
        confirmVerified(searchIndexAdmin)
    }

    @Test
    fun `every found instance is indexed`() {
        stubDataStoreToContain(listOf(instance1, instance2, instance3))

        cut.run()

        verify(atLeast = 1) { searchIndexAdmin.toString() }
        verifyOrder {
            searchIndexAdmin.reset()
            searchIndexAdmin.index(instance1)
            searchIndexAdmin.index(instance2)
            searchIndexAdmin.index(instance3)
        }
        confirmVerified(searchIndexAdmin)
    }

    abstract fun `task steps are logged`(log: LogRecord)

    protected fun executeTaskStepsAreLoggedTest(log: LogRecord) {
        stubDataStoreToContain(listOf(instance1, instance2))

        cut.run()

        with(log.messages) {
            assertThat(get(0)).isEqualTo("Reconstructing '$searchIndexAdmin':")
            assertThat(get(1)).isEqualTo("Resetting '$searchIndexAdmin':")
            assertThat(get(2)).containsSubsequence("Reset of '$searchIndexAdmin' finished. Took ", "ms.")
            assertThat(get(3)).isEqualTo("Repopulating '$searchIndexAdmin':")
            assertThat(get(4)).isEqualTo("Indexing [${expectedShortDescription(instance1)}]")
            assertThat(get(5)).isEqualTo("Indexing [${expectedShortDescription(instance2)}]")
            assertThat(get(6)).containsSubsequence("Repopulation of '$searchIndexAdmin' finished. Took ", "ms.")
            assertThat(get(7)).containsSubsequence("Reconstruction of '$searchIndexAdmin' finished. Took ", "ms.")
        }
    }

}
