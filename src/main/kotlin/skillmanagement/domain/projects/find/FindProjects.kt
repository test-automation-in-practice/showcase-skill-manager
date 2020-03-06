package skillmanagement.domain.projects.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.projects.Project

@BusinessFunction
class FindProjects(
    private val findProjectsInDataStore: FindProjectsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(): List<Project> {
        return findProjectsInDataStore()
    }

}
