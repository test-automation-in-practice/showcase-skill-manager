package skillmanagement.domain.employees.model

import skillmanagement.common.events.Event

/**
 * Tagging interface for _all_ [EmployeeEntity] related event types.
 */
internal interface EmployeeEvent : Event

internal data class EmployeeAddedEvent(val employee: EmployeeEntity) : EmployeeEvent
internal data class EmployeeUpdatedEvent(val employee: EmployeeEntity) : EmployeeEvent
internal data class EmployeeDeletedEvent(val employee: EmployeeEntity) : EmployeeEvent

internal data class SkillUpdatedEvent(val skill: SkillData) : EmployeeEvent
internal data class SkillDeletedEvent(val skill: SkillData) : EmployeeEvent

internal data class ProjectUpdatedEvent(val project: ProjectData) : EmployeeEvent
internal data class ProjectDeletedEvent(val project: ProjectData) : EmployeeEvent
