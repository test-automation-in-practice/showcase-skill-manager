package skillmanagement.domain.projects.model

import skillmanagement.domain.Event

data class ProjectAddedEvent(val project: Project) : Event
data class ProjectUpdatedEvent(val project: Project) : Event
data class ProjectDeletedEvent(val project: Project) : Event
