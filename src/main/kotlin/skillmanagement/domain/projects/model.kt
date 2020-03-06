package skillmanagement.domain.projects

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class Project(
    val id: UUID,
    val label: ProjectLabel,
    val description: ProjectDescription
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
