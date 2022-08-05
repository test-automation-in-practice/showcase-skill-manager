package skillmanagement.domain.projects.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectRepresentation
import skillmanagement.domain.projects.model.toRepresentations

@GraphQLAdapter
internal class GetProjectsPageGraphQLAdapter(
    private val delegate: GetProjectsPageFunction
) {

    @QueryMapping
    fun getProjectsPage(@Argument pageIndex: PageIndex, @Argument pageSize: PageSize): Page<ProjectRepresentation> {
        val pagination = Pagination(pageIndex, pageSize)
        val allProjectsQuery = AllProjectsQuery(pagination)
        val result = delegate(allProjectsQuery)

        return result.toRepresentations()
    }

}
