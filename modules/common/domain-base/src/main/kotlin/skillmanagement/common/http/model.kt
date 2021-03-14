package skillmanagement.common.http

import org.springframework.hateoas.PagedModel
import skillmanagement.common.model.Page

fun Page<*>.toMetaData(): PagedModel.PageMetadata =
    PagedModel.PageMetadata(pageSize.toLong(), pageIndex.toLong(), totalElements)
