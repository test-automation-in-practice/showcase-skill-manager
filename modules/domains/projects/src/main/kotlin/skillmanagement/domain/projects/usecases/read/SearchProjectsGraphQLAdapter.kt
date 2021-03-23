package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.Pagination
import skillmanagement.common.model.Page
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project

@GraphQLAdapter
internal class SearchProjectsGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun searchProjects(query: String, pagination: Pagination?): Page<Project> =
        getProjectsPage(query(query, pagination ?: Pagination.DEFAULT))

    private fun query(query: String, pagination: Pagination) =
        ProjectsMatchingQuery(pagination.index, pagination.size, query)

}
