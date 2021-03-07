package skillmanagement.common.searchindices

import skillmanagement.common.model.Suggestion
import java.util.UUID

interface SearchIndex<T : Any> {
    fun index(instance: T)
    fun deleteById(id: UUID)

    fun query(query: PagedStringQuery): Page<UUID>
    fun findAll(query: PagedFindAllQuery): Page<UUID>
    fun suggest(input: String, max: MaxSuggestions): List<Suggestion>
}