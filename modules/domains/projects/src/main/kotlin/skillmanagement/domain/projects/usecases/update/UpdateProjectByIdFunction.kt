package skillmanagement.domain.projects.usecases.update

import arrow.core.Either
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.common.success
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.ProjectUpdatedEvent
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotChanged
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotFound

@BusinessFunction
class UpdateProjectByIdFunction internal constructor(
    private val getProjectById: GetProjectByIdFunction,
    private val updateProjectInDataStore: UpdateProjectInDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    @RetryOnConcurrentProjectUpdate
    operator fun invoke(
        projectId: ProjectId,
        block: (Project) -> Project
    ): Either<ProjectUpdateFailure, ProjectEntity> {
        val currentProject = getProjectById(projectId) ?: return failure(ProjectNotFound)
        val modifiedProject = currentProject.update(block)

        if (modifiedProject == currentProject) return failure(ProjectNotChanged(currentProject))

        val updatedProject = updateProjectInDataStore(modifiedProject)
        publishEvent(ProjectUpdatedEvent(updatedProject))
        return success(updatedProject)
    }

}

sealed class ProjectUpdateFailure {
    object ProjectNotFound : ProjectUpdateFailure()
    data class ProjectNotChanged(val project: ProjectEntity) : ProjectUpdateFailure()
}
