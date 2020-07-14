package skillmanagement.domain.projects

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.time.Instant
import java.util.UUID

data class Project(
    val id: UUID,
    val version: Int,
    val label: ProjectLabel,
    val description: ProjectDescription,
    val lastUpdate: Instant
)

data class ProjectDescription @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class ProjectLabel @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}
