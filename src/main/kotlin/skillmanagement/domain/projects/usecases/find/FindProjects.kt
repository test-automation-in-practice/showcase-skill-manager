package skillmanagement.domain.projects.usecases.find

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex
import skillmanagement.domain.projects.usecases.get.GetProjectFromDataStore

@BusinessFunction
class FindProjects(
    private val findAllProjectsInDataStore: FindAllProjectsInDataStore,
    private val getProjectFromDataStore: GetProjectFromDataStore,
    private val searchIndex: ProjectSearchIndex
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindProjectsQuery = NoOpQuery): List<Project> =
        when (query) {
            is ProjectsMatchingQuery -> {
                val ids = searchIndex.query(query.queryString)
                getProjectFromDataStore(ids).values.toList()
            }
            NoOpQuery -> findAllProjectsInDataStore()
        }

}

sealed class FindProjectsQuery
data class ProjectsMatchingQuery(val queryString: String) : FindProjectsQuery()
object NoOpQuery : FindProjectsQuery()
