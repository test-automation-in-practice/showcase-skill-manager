package skillmanagement.domain.skills

import skillmanagement.domain.Event

data class SkillDeletedEvent(val skill: Skill) : Event
