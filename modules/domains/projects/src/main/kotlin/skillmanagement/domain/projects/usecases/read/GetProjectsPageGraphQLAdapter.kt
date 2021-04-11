package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectEntity

@GraphQLAdapter
internal class GetProjectsPageGraphQLAdapter(
    private val getProjectsPage: GetProjectsPageFunction
) : GraphQLQueryResolver {

    fun getProjectsPage(pagination: Pagination?): Page<ProjectEntity> =
        getProjectsPage(AllProjectsQuery(pagination ?: Pagination.DEFAULT))

}
