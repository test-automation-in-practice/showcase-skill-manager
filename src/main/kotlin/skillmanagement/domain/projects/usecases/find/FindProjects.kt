package skillmanagement.domain.projects.usecases.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.projects.model.Project

@BusinessFunction
class FindProjects(
    private val findProjectsInDataStore: FindProjectsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindProjectsQuery = NoOpQuery): List<Project> {
        return findProjectsInDataStore(query)
    }

}

sealed class FindProjectsQuery
data class ProjectsWithLabelLike(val searchTerms: String) : FindProjectsQuery()
object NoOpQuery : FindProjectsQuery()
