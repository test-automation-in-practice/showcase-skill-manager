package skillmanagement.domain.employees.usecases.projectassignments.create

import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.ProjectId

interface GetProjectByIdAdapterFunction {
    operator fun invoke(id: ProjectId): ProjectData?
}
