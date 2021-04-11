package skillmanagement.common.searchindices

import mu.KLogger
import skillmanagement.common.model.Entity
import kotlin.system.measureTimeMillis

abstract class AbstractReconstructSearchIndexTask<T : Entity<*>> : Runnable {

    protected abstract val log: KLogger
    protected abstract val searchIndexAdmin: SearchIndexAdmin<T>

    override fun run() {
        log.info { "Reconstructing '$searchIndexAdmin':" }
        val resetDuration = resetIndex()
        val indexingDuration = repopulateIndex()
        log.info { "Reconstruction of '$searchIndexAdmin' finished. Took ${resetDuration + indexingDuration}ms." }
    }

    private fun resetIndex(): Long {
        log.debug { "Resetting '$searchIndexAdmin':" }
        val duration = measureTimeMillis {
            searchIndexAdmin.reset()
        }
        log.debug { "Reset of '$searchIndexAdmin' finished. Took ${duration}ms." }
        return duration
    }

    private fun repopulateIndex(): Long {
        log.debug { "Repopulating '$searchIndexAdmin':" }
        val duration = measureTimeMillis {
            executeForAllInstancesInDataStore { instance ->
                log.debug { "Indexing [${shortDescription(instance)}]" }
                searchIndexAdmin.index(instance)
            }
        }
        log.debug { "Repopulation of '$searchIndexAdmin' finished. Took ${duration}ms." }
        return duration
    }

    protected abstract fun executeForAllInstancesInDataStore(callback: (T) -> Unit)
    protected abstract fun shortDescription(instance: T): String

}
