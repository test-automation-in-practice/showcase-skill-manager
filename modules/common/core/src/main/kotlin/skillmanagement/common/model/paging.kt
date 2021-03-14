package skillmanagement.common.model

data class Page<T : Any>(
    val content: List<T>,
    val pageIndex: Int,
    val pageSize: Int,
    val totalElements: Long
) : List<T> by content {

    fun hasPrevious(): Boolean = pageIndex > 0
    fun hasNext(): Boolean = pageIndex < totalElements / pageSize

    fun <C : Any> withOtherContent(content: List<C>) = Page(
        content = content,
        pageIndex = pageIndex,
        pageSize = pageSize,
        totalElements = totalElements
    )

}

class PageIndex(value: Int) : IntType(value, min = 0) {
    companion object {
        val DEFAULT = PageIndex(0)
    }
}

class PageSize(value: Int) : IntType(value, min = 1, max = 10_000) {
    companion object {
        val DEFAULT = PageSize(100)
        val MAX = PageSize(10_000)
    }
}

fun <T : Any> emptyPage(index: Int = 0, size: Int = 100): Page<T> = Page(
    content = emptyList(),
    pageIndex = index,
    pageSize = size,
    totalElements = 0
)

fun <T : Any> pageOf(elements: List<T>, index: Int = 0, size: Int = 100, totalElements: Long = elements.size.toLong()) =
    Page(
        content = elements.toList(),
        pageIndex = index,
        pageSize = size,
        totalElements = totalElements
    )
