package skillmanagement.domain.projects.get

import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDocument
import skillmanagement.domain.projects.ProjectRepository
import skillmanagement.domain.projects.toProject
import skillmanagement.domain.TechnicalFunction
import java.util.*

@TechnicalFunction
class GetProjectFromDataStore(
    private val repository: ProjectRepository
) {

    operator fun invoke(id: UUID): Project? = repository.findById(id)
        .map(ProjectDocument::toProject)
        .orElse(null)

    operator fun invoke(ids: Collection<UUID>): Set<Project> = repository.findAllById(ids)
        .map(ProjectDocument::toProject)
        .toSet()

}
