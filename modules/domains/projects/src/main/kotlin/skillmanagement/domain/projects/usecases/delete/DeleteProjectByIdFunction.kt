package skillmanagement.domain.projects.usecases.delete

import skillmanagement.common.events.PublishEvent
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import skillmanagement.domain.projects.usecases.get.GetProjectByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteProjectByIdFunction(
    private val getProjectById: GetProjectByIdFunction,
    private val deleteProjectFromDataStore: DeleteProjectFromDataStoreFunction,
    private val publishEvent: PublishEvent
) {

    // TODO: Security - Only invokable by Project-Admins
    operator fun invoke(id: UUID): DeleteProjectByIdResult {
        val project = getProjectById(id) ?: return ProjectNotFound
        deleteProjectFromDataStore(id)
        publishEvent(ProjectDeletedEvent(project))
        return SuccessfullyDeleted
    }

}

sealed class DeleteProjectByIdResult {
    object ProjectNotFound : DeleteProjectByIdResult()
    object SuccessfullyDeleted : DeleteProjectByIdResult()
}
