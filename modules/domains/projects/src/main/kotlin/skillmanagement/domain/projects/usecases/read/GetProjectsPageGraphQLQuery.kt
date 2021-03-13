package skillmanagement.domain.projects.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.domain.projects.model.Project

@Component
class GetProjectsPageGraphQLQuery(
    private val getProjects: GetProjectsFunction
) : GraphQLQueryResolver {

    fun getProjectsPage(page: Int, size: Int): List<Project> = withErrorHandling {
        getProjects(AllProjectsQuery(PageIndex(page), PageSize(size)))
    }

}
