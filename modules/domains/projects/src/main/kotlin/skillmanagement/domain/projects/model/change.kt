package skillmanagement.domain.projects.model

internal data class ProjectChangeData(
    val label: ProjectLabel,
    val description: ProjectDescription
)

internal fun Project.toChangeData(): ProjectChangeData =
    ProjectChangeData(label = label, description = description)

internal fun Project.merge(changes: ProjectChangeData): Project =
    copy(label = changes.label, description = changes.description)
