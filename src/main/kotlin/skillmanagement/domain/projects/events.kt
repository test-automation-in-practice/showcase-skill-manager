package skillmanagement.domain.projects

import skillmanagement.domain.Event

data class ProjectDeletedEvent(val project: Project) : Event
