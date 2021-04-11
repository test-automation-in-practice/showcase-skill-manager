package skillmanagement.domain.skills.model

import skillmanagement.common.events.Event

/**
 * Tagging interface for _all_ [SkillEntity] related event types.
 */
internal interface SkillEvent : Event

internal data class SkillAddedEvent(val skill: SkillEntity) : SkillEvent
internal data class SkillUpdatedEvent(val skill: SkillEntity) : SkillEvent
internal data class SkillDeletedEvent(val skill: SkillEntity) : SkillEvent
