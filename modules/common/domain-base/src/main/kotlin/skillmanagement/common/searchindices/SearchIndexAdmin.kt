package skillmanagement.common.searchindices

import skillmanagement.common.model.Entity

interface SearchIndexAdmin<T : Entity<*>> {
    fun index(instance: T)
    fun reset()
    fun refresh()
}
