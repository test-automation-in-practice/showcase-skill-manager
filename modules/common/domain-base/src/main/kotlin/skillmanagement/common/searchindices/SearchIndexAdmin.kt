package skillmanagement.common.searchindices

interface SearchIndexAdmin<T : Any> : SearchIndex<T> {
    fun reset()
    fun refresh()
}