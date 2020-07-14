package skillmanagement.domain.skills

import skillmanagement.domain.Event

data class SkillAddedEvent(val skill: Skill) : Event
data class SkillUpdatedEvent(val skill: Skill) : Event
data class SkillDeletedEvent(val skill: Skill) : Event
