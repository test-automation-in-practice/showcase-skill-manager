package skillmanagement.test.tasks

import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.searchindices.SearchIndexAdmin

abstract class AbstractReconstructSearchIndexTaskTests<T : Any> {

    abstract val searchIndex: SearchIndexAdmin<T>
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

        verify(atLeast = 1) { searchIndex.toString() }
        verify { searchIndex.reset() }
        confirmVerified(searchIndex)
    }

    @Test
    fun `every found instance is indexed`() {
        stubDataStoreToContain(listOf(instance1, instance2, instance3))

        cut.run()

        verify(atLeast = 1) { searchIndex.toString() }
        verifyOrder {
            searchIndex.reset()
            searchIndex.index(instance1)
            searchIndex.index(instance2)
            searchIndex.index(instance3)
        }
        confirmVerified(searchIndex)
    }

    abstract fun `task steps are logged`(log: LogRecord)

    protected fun executeTaskStepsAreLoggedTest(log: LogRecord) {
        stubDataStoreToContain(listOf(instance1, instance2))

        cut.run()

        with(log.messages) {
            Assertions.assertThat(get(0)).isEqualTo("Reconstructing '$searchIndex':")
            Assertions.assertThat(get(1)).isEqualTo("Resetting '$searchIndex':")
            Assertions.assertThat(get(2)).containsSubsequence("Reset of '$searchIndex' finished. Took ", "ms.")
            Assertions.assertThat(get(3)).isEqualTo("Repopulating '$searchIndex':")
            Assertions.assertThat(get(4)).isEqualTo("Indexing [${expectedShortDescription(instance1)}]")
            Assertions.assertThat(get(5)).isEqualTo("Indexing [${expectedShortDescription(instance2)}]")
            Assertions.assertThat(get(6)).containsSubsequence("Repopulation of '$searchIndex' finished. Took ", "ms.")
            Assertions.assertThat(get(7)).containsSubsequence("Reconstruction of '$searchIndex' finished. Took ", "ms.")
        }
    }

}
