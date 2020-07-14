package skillmanagement.domain.projects.update

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.get.GetProjectById
import skillmanagement.domain.projects.update.UpdateProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.update.UpdateProjectByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class UpdateProjectById(
    private val getProjectById: GetProjectById,
    private val updateProjectInDataStore: UpdateProjectInDataStore
) {

    // TODO: Security - Who can change Projects?
    @RetryOnConcurrentProjectUpdate
    operator fun invoke(projectId: UUID, block: (Project) -> Project): UpdateProjectByIdResult {
        val currentProject = getProjectById(projectId) ?: return ProjectNotFound
        val modifiedProject = block(currentProject)

        assertNoInvalidModifications(currentProject, modifiedProject)

        val updatedProject = updateProjectInDataStore(modifiedProject)
        return SuccessfullyUpdated(updatedProject)
    }

    private fun assertNoInvalidModifications(currentProject: Project, modifiedProject: Project) {
        check(currentProject.id == modifiedProject.id) { "ID must not be changed!" }
        check(currentProject.version == modifiedProject.version) { "Version must not be changed!" }
        check(currentProject.lastUpdate == modifiedProject.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class UpdateProjectByIdResult {
    object ProjectNotFound : UpdateProjectByIdResult()
    data class SuccessfullyUpdated(val project: Project) : UpdateProjectByIdResult()
}
