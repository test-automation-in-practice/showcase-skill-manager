package skillmanagement.domain.skills.usecases.create

import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillCreationData
import java.time.Clock

@BusinessFunction
class CreateSkillFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val insertSkillIntoDataStore: InsertSkillIntoDataStoreFunction,
    private val publishEvent: PublishEventFunction,
    private val clock: Clock
) {

    operator fun invoke(data: SkillCreationData): Skill {
        val skill = data.toSkill()
        insertSkillIntoDataStore(skill)
        publishEvent(SkillAddedEvent(skill))
        return skill
    }

    private fun SkillCreationData.toSkill() =
        Skill(
            id = idGenerator.generateId(),
            version = 1,
            label = label,
            description = description,
            tags = tags,
            lastUpdate = clock.instant()
        )

}
