package skillmanagement.test.tasks

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsInOrder
import io.mockk.confirmVerified
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import skillmanagement.common.model.Entity
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.searchindices.SearchIndexAdmin

abstract class AbstractReconstructSearchIndexTaskTests<T : Entity<*>> {

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

        assertThat(log) containsInOrder {
            info("Reconstructing '$searchIndexAdmin':")
            debug("Resetting '$searchIndexAdmin':")
            debug(containsInOrder("Reset of '$searchIndexAdmin' finished. Took ", "ms."))
            debug("Repopulating '$searchIndexAdmin':")
            debug("Indexing [${expectedShortDescription(instance1)}]")
            debug("Indexing [${expectedShortDescription(instance2)}]")
            debug(containsInOrder("Repopulation of '$searchIndexAdmin' finished. Took ", "ms."))
            info(containsInOrder("Reconstruction of '$searchIndexAdmin' finished. Took ", "ms."))
        }
    }

}
