package skillmanagement.common.searchindices

import skillmanagement.common.model.IntType
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize

interface PagedFindAllQuery {
    val pageIndex: PageIndex
    val pageSize: PageSize
}

interface PagedStringQuery {
    val pageIndex: PageIndex
    val pageSize: PageSize
    val queryString: String
}

class MaxSuggestions(value: Int) : IntType(value, min = 1, max = 10_000) {
    companion object {
        val DEFAULT = MaxSuggestions(100)
        val MAX = MaxSuggestions(10_000)

        fun of(value: Int?): MaxSuggestions =
            value?.let(::MaxSuggestions) ?: DEFAULT
    }
}
