package skillmanagement.domain.projects

import skillmanagement.domain.Event

data class ProjectAddedEvent(val project: Project) : Event
data class ProjectDeletedEvent(val project: Project) : Event
