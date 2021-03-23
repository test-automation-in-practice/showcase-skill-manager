package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.Pagination
import skillmanagement.common.model.Page
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.Project

@GraphQLAdapter
internal class GetProjectsPageGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun getProjectsPage(pagination: Pagination?): Page<Project> =
        getProjectsPage(query(pagination ?: Pagination.DEFAULT))

    private fun query(pagination: Pagination) = AllProjectsQuery(pagination.index, pagination.size)

}
