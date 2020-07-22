package skillmanagement.domain.skills.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.PublishEvent
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillAddedEvent
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.Tag
import java.time.Clock
import java.util.SortedSet

@BusinessFunction
class AddSkill(
    private val idGenerator: IdGenerator,
    private val insertSkillIntoDataStore: InsertSkillIntoDataStore,
    private val publishEvent: PublishEvent,
    private val clock: Clock
) {

    // TODO: Security - Only invokable by Skill-Admins
    operator fun invoke(label: SkillLabel, tags: SortedSet<Tag>): Skill {
        val skill = Skill(
            id = idGenerator.generateId(),
            version = 1,
            label = label,
            tags = tags,
            lastUpdate = clock.instant()
        )
        insertSkillIntoDataStore(skill)
        publishEvent(SkillAddedEvent(skill))
        return skill
    }

}
