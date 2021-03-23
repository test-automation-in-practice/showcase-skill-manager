package skillmanagement.common.graphql

import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize

data class Pagination(
    val index: PageIndex = PageIndex.DEFAULT,
    val size: PageSize = PageSize.DEFAULT
) {
    companion object {
        val DEFAULT = Pagination()
    }
}
