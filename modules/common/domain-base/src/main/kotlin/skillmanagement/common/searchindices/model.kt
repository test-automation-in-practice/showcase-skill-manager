package skillmanagement.common.searchindices

import skillmanagement.common.model.IntType
import skillmanagement.common.model.Pagination

interface PagedFindAllQuery {
    val pagination: Pagination
}

interface PagedStringQuery {
    val queryString: String
    val pagination: Pagination
}

class MaxSuggestions(value: Int) : IntType(value, min = 1, max = 10_000) {
    companion object {
        val DEFAULT = MaxSuggestions(100)
        val MAX = MaxSuggestions(10_000)

        fun of(value: Int?): MaxSuggestions =
            value?.let(::MaxSuggestions) ?: DEFAULT
    }
}
