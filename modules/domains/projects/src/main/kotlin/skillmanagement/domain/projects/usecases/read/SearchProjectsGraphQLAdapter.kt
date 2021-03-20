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
internal class SearchProjectsGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun searchProjects(query: String, pagination: Pagination?): Page<Project> = withErrorHandling {
        getProjectsPage(query(query, pagination))
    }

    private fun query(query: String, pagination: Pagination?) =
        ProjectsMatchingQuery(
            queryString = query,
            pageIndex = PageIndex.of(pagination?.index),
            pageSize = PageSize.of(pagination?.size)
        )

}
