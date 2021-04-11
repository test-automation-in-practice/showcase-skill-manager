package skillmanagement.domain.projects.model

import skillmanagement.common.model.Entity
import skillmanagement.common.model.IdType
import skillmanagement.common.model.Label
import skillmanagement.common.model.Text
import java.time.Instant
import java.util.UUID

data class ProjectEntity(
    override val id: ProjectId,
    override val version: Int,
    val label: ProjectLabel,
    val description: ProjectDescription,
    override val lastUpdate: Instant
) : Entity<ProjectId>

class ProjectId(value: UUID) : IdType(value)
class ProjectLabel(value: String) : Label(value)
class ProjectDescription(value: String) : Text(value)

fun projectId(value: String) = ProjectId(UUID.fromString(value))
