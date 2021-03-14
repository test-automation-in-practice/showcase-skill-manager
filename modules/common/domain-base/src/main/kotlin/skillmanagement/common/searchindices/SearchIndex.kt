package skillmanagement.common.searchindices

import skillmanagement.common.model.Page
import skillmanagement.common.model.Suggestion
import java.util.UUID

/**
 * Search indices are used to store search related data about entities and make
 * it possible to query for those entities in a number of ways:
 *
 * - paged iteration over all entities
 * - querying with full _Lucene_ query syntax support
 * - suggesting relevant entities based on user input
 *
 * Note however that a search index will not contain the all of the entity data
 * and therefore cannot be used on its own. If entity data is needed, the
 * original data source needs to be queried with the IDs returned by the search
 * index.
 */
interface SearchIndex<T : Any> {

    /**
     * Create or update an index document for the given [T] instance in the
     * [SearchIndex].
     *
     * Depending on how the [SearchIndex] is configured the change will not
     * take effect until the regular refresh cycle was executed.
     */
    fun index(instance: T)

    /**
     * Deletes the index document with the given ID from the [SearchIndex].
     *
     * Depending on how the [SearchIndex] is configured the change will not
     * take effect until the regular refresh cycle was executed.
     */
    fun deleteById(id: UUID)

    fun query(query: PagedStringQuery): Page<UUID>
    fun findAll(query: PagedFindAllQuery): Page<UUID>
    fun suggest(input: String, max: MaxSuggestions = MaxSuggestions.DEFAULT): List<Suggestion>

}
