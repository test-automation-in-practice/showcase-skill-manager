package skillmanagement.domain.projects.add

import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectRepository
import skillmanagement.domain.projects.toDocument
import skillmanagement.domain.TechnicalFunction

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val repository: ProjectRepository
) {

    operator fun invoke(project: Project) {
        repository.insert(project.toDocument())
    }

}
