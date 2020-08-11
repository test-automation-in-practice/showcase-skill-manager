package skillmanagement.domain.skills.searchindex

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.SkillUpdatedEvent

@Component
class SkillSearchIndexUpdatingEventListener(
    private val searchIndex: SkillSearchIndex
) {

    @Async
    @EventListener
    fun handle(event: SkillAddedEvent) {
        searchIndex.index(event.skill)
    }

    @Async
    @EventListener
    fun handle(event: SkillUpdatedEvent) {
        searchIndex.index(event.skill)
    }

    @Async
    @EventListener
    fun handle(event: SkillDeletedEvent) {
        searchIndex.deleteById(event.skill.id)
    }

}
