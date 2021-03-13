package skillmanagement.domain.skills.usecases.create

import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import java.time.Clock
import java.util.Collections.emptySortedSet
import java.util.SortedSet

@BusinessFunction
class CreateSkillFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val insertSkillIntoDataStore: InsertSkillIntoDataStoreFunction,
    private val publishEvent: PublishEventFunction,
    private val clock: Clock
) {

    operator fun invoke(
        label: SkillLabel,
        description: SkillDescription? = null,
        tags: SortedSet<Tag> = emptySortedSet()
    ): Skill {
        val skill = skill(label, description, tags)
        insertSkillIntoDataStore(skill)
        publishEvent(SkillAddedEvent(skill))
        return skill
    }

    private fun skill(
        label: SkillLabel,
        description: SkillDescription?,
        tags: SortedSet<Tag>
    ) = Skill(
        id = idGenerator.generateId(),
        version = 1,
        label = label,
        description = description,
        tags = tags,
        lastUpdate = clock.instant()
    )

}
