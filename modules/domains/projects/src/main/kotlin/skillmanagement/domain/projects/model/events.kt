package skillmanagement.domain.projects.model

import skillmanagement.common.events.Event

/**
 * Tagging interface for _all_ [Project] related event types.
 */
internal interface ProjectEvent : Event

internal data class ProjectAddedEvent(val project: Project) : ProjectEvent
internal data class ProjectUpdatedEvent(val project: Project) : ProjectEvent
internal data class ProjectDeletedEvent(val project: Project) : ProjectEvent
