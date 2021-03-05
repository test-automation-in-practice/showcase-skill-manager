package skillmanagement.domain.employees.gateways

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import java.util.UUID

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
    operator fun invoke(id: UUID): ProjectData? = getProjectById(id)?.toData()
    private fun Project.toData() = ProjectData(id = id, label = label.toString(), description = description.toString())
}