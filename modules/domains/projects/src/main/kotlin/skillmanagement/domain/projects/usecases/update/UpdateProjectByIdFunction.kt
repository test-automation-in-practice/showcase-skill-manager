package skillmanagement.domain.projects.usecases.update

import arrow.core.Either
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.common.success
import skillmanagement.domain.projects.model.Project
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
    operator fun invoke(projectId: ProjectId, block: (Project) -> Project): Either<ProjectUpdateFailure, Project> {
        val currentProject = getProjectById(projectId) ?: return failure(ProjectNotFound)
        val modifiedProject = block(currentProject)

        if (modifiedProject == currentProject) return failure(ProjectNotChanged(currentProject))
        assertNoInvalidModifications(currentProject, modifiedProject)

        val updatedProject = updateProjectInDataStore(modifiedProject)
        publishEvent(ProjectUpdatedEvent(updatedProject))
        return success(updatedProject)
    }

    private fun assertNoInvalidModifications(currentProject: Project, modifiedProject: Project) {
        check(currentProject.id == modifiedProject.id) { "ID must not be changed!" }
        check(currentProject.version == modifiedProject.version) { "Version must not be changed!" }
        check(currentProject.lastUpdate == modifiedProject.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class ProjectUpdateFailure {
    object ProjectNotFound : ProjectUpdateFailure()
    data class ProjectNotChanged(val project: Project) : ProjectUpdateFailure()
}
