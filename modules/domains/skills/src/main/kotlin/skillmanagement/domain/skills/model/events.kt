package skillmanagement.domain.skills.model

import skillmanagement.common.events.Event

/**
 * Tagging interface for _all_ [Skill] related event types.
 */
internal interface SkillEvent : Event

internal data class SkillAddedEvent(val skill: Skill) : SkillEvent
internal data class SkillUpdatedEvent(val skill: Skill) : SkillEvent
internal data class SkillDeletedEvent(val skill: Skill) : SkillEvent
