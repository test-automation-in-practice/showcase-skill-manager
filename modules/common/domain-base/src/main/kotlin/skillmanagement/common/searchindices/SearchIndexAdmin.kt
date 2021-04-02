package skillmanagement.common.searchindices

interface SearchIndexAdmin<T : Any> {
    fun index(instance: T)
    fun reset()
    fun refresh()
}
