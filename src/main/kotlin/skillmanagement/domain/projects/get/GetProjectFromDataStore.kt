package skillmanagement.domain.projects.get

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDocument
import skillmanagement.domain.projects.ProjectRepository
import skillmanagement.domain.projects.toProject
import java.util.*

@TechnicalFunction
class GetProjectFromDataStore(
    private val repository: ProjectRepository
) {

    operator fun invoke(id: UUID): Project? = repository.findById(id)
        .map(ProjectDocument::toProject)
        .orElse(null)

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Project> = repository.findAllById(ids)
        .map(ProjectDocument::toProject)
        .map { it.id to it }
        .toMap()

}
