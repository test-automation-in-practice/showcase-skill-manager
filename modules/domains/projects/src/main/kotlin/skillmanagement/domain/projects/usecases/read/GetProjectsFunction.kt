package skillmanagement.domain.projects.usecases.read

import skillmanagement.common.search.Page
import skillmanagement.common.search.PageIndex
import skillmanagement.common.search.PageSize
import skillmanagement.common.search.PagedFindAllQuery
import skillmanagement.common.search.PagedStringQuery
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex

@BusinessFunction
class GetProjectsFunction(
    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction,
    private val searchIndex: ProjectSearchIndex
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: ProjectsQuery): Page<Project> {
        val page = when (query) {
            is ProjectsMatchingQuery -> searchIndex.query(query)
            is AllProjectsQuery -> searchIndex.findAll(query)
        }
        val projectsMap = getProjectsFromDataStore(page.content)
        val projects = page.content.mapNotNull { projectsMap[it] }
        return page.withOtherContent(projects)
    }

}
