package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project

@GraphQLAdapter
internal class SearchProjectsGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun searchProjects(query: String, index: Int?, size: Int?): Page<Project> = withErrorHandling {
        getProjectsPage(query(query, index, size))
    }

    private fun query(query: String, index: Int?, size: Int?) =
        ProjectsMatchingQuery(queryString = query, pageIndex = pageIndex(index), pageSize = pageSize(size))

    private fun pageIndex(index: Int?) = index?.let(::PageIndex) ?: PageIndex.DEFAULT
    private fun pageSize(size: Int?) = size?.let(::PageSize) ?: PageSize.DEFAULT

}
