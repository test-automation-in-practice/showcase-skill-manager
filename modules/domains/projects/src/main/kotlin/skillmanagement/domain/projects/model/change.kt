@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.projects.model

internal data class ProjectChangeData(
    val label: ProjectLabel,
    val description: ProjectDescription
)

internal fun ProjectEntity.toChangeData(): ProjectChangeData =
    ProjectChangeData(label = label, description = description)

internal fun ProjectEntity.merge(changes: ProjectChangeData): ProjectEntity =
    copy(label = changes.label, description = changes.description)
