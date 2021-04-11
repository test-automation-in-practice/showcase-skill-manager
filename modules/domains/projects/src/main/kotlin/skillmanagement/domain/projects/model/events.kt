package skillmanagement.domain.projects.model

import skillmanagement.common.events.Event

/**
 * Tagging interface for _all_ [ProjectEntity] related event types.
 */
internal interface ProjectEvent : Event

internal data class ProjectAddedEvent(val project: ProjectEntity) : ProjectEvent
internal data class ProjectUpdatedEvent(val project: ProjectEntity) : ProjectEvent
internal data class ProjectDeletedEvent(val project: ProjectEntity) : ProjectEvent
