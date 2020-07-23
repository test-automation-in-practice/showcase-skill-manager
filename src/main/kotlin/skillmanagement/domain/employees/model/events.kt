package skillmanagement.domain.employees.model

import skillmanagement.common.events.Event

data class EmployeeAddedEvent(val employee: Employee) : Event
data class EmployeeUpdatedEvent(val employee: Employee) : Event
data class EmployeeDeletedEvent(val employee: Employee) : Event
