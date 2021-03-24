@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.projects.model

data class ProjectCreationData(
    val label: ProjectLabel,
    val description: ProjectDescription
)
