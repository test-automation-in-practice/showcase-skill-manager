package skillmanagement.domain.projects.delete

import skillmanagement.domain.projects.ProjectRepository
import skillmanagement.domain.TechnicalFunction
import java.util.*

@TechnicalFunction
class DeleteProjectFromDataStore(
    private val repository: ProjectRepository
) {

    operator fun invoke(id: UUID) {
        repository.deleteById(id)
    }

}
