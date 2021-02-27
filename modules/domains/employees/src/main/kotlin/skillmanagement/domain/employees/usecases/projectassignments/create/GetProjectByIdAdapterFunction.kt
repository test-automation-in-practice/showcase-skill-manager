package skillmanagement.domain.employees.usecases.projectassignments.create

import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.usecases.get.GetProjectByIdFunction
import java.util.UUID

@TechnicalFunction
class GetProjectByIdAdapterFunction(
    private val getProjectById: GetProjectByIdFunction
) {
    operator fun invoke(id: UUID): ProjectData? = getProjectById(id)?.toData()
    private fun Project.toData() = ProjectData(id = id, label = label.toString(), description = description.toString())
}