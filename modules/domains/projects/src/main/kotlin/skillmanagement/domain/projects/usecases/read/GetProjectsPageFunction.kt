package skillmanagement.domain.projects.usecases.read

import skillmanagement.common.model.Page
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectId

@BusinessFunction
class GetProjectsPageFunction internal constructor(
    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction,
    private val searchIndex: SearchIndex<ProjectEntity, ProjectId>
) {

    operator fun invoke(query: ProjectsQuery): Page<ProjectEntity> {
        val page = when (query) {
            is ProjectsMatchingQuery -> searchIndex.query(query)
            is AllProjectsQuery -> searchIndex.findAll(query)
        }
        val projectsMap = getProjectsFromDataStore(page.content)
        val projects = page.mapNotNull { projectsMap[it] }
        return page.withOtherContent(projects)
    }

}
