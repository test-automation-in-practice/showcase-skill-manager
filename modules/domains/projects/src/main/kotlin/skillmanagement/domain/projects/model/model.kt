package skillmanagement.domain.projects.model

import skillmanagement.common.model.IdType
import skillmanagement.common.model.Label
import skillmanagement.common.model.Text
import java.time.Instant
import java.util.UUID

data class Project(
    val id: ProjectId,
    val version: Int,
    val label: ProjectLabel,
    val description: ProjectDescription,
    val lastUpdate: Instant
)

class ProjectId(value: UUID) : IdType(value)
class ProjectLabel(value: String) : Label(value)
class ProjectDescription(value: String) : Text(value)

fun projectId(value: String) = ProjectId(UUID.fromString(value))
