package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.Pagination
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project

@GraphQLAdapter
internal class GetProjectsPageGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun getProjectsPage(pagination: Pagination?): Page<Project> = withErrorHandling {
        getProjectsPage(query(pagination))
    }

    private fun query(pagination: Pagination?) =
        AllProjectsQuery(
            pageIndex = pagination?.index?.let(::PageIndex) ?: PageIndex.DEFAULT,
            pageSize = pagination?.size?.let(::PageSize) ?: PageSize.DEFAULT
        )

}
