package skillmanagement.domain.skills.usecases.create

import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import java.time.Clock

@BusinessFunction
class CreateSkillFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val insertSkillIntoDataStore: InsertSkillIntoDataStoreFunction,
    private val publishEvent: PublishEventFunction,
    private val clock: Clock
) {

    operator fun invoke(data: SkillCreationData): SkillEntity {
        val skill = data.toSkill()
        insertSkillIntoDataStore(skill)
        publishEvent(SkillAddedEvent(skill))
        return skill
    }

    private fun SkillCreationData.toSkill(): SkillEntity {
        val now = clock.instant()
        return SkillEntity(
            id = SkillId(idGenerator.generateId()),
            version = 1,
            data = Skill(
                label = label,
                description = description,
                tags = tags
            ),
            created = now,
            lastUpdate = now
        )
    }

}
