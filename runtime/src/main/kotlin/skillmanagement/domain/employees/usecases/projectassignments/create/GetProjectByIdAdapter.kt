package skillmanagement.domain.employees.usecases.projectassignments.create

import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.usecases.get.GetProjectById
import java.util.UUID

@TechnicalFunction
class GetProjectByIdAdapter(
    private val getProjectById: GetProjectById
) {
    operator fun invoke(id: UUID): ProjectData? = getProjectById(id)?.toData()
    private fun Project.toData() = ProjectData(id = id, label = label, description = description)
}