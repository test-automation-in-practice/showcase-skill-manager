package skillmanagement.domain.employees.gateways

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.ProjectId
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import skillmanagement.domain.projects.model.ProjectId as ExternalProjectId

/**
 * This adapter function provides an abstraction for getting [ProjectData] from
 * the _projects_ domain module.
 *
 * This is done in order to reduce the coupling between different domains as
 * much as possible. If this project were ever to be split into multiple
 * runtimes, this function would need to be changed to a network call
 * (e.g. HTTP or RSocket etc.).
 */
@BusinessFunction
internal class GetProjectByIdAdapterFunction(
    private val getProjectById: GetProjectByIdFunction
) {

    operator fun invoke(id: ProjectId): ProjectData? =
        getProjectById(projectId(id))?.toData()

    private fun ProjectEntity.toData() = ProjectData(
        id = projectId(id),
        label = data.label.toString(),
        description = data.description.toString()
    )

}

internal fun projectId(id: ProjectId) = ExternalProjectId(id.toUUID())
internal fun projectId(id: ExternalProjectId) = ProjectId(id.toUUID())
