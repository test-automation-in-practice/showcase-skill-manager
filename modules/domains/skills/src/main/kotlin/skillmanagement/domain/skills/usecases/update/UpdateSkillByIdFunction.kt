package skillmanagement.domain.skills.usecases.update

import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillUpdatedEvent
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.usecases.update.UpdateSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.usecases.update.UpdateSkillByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class UpdateSkillByIdFunction internal constructor(
    private val getSkillById: GetSkillByIdFunction,
    private val updateSkillInDataStore: UpdateSkillInDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    @RetryOnConcurrentSkillUpdate
    operator fun invoke(skillId: UUID, block: (Skill) -> Skill): UpdateSkillByIdResult {
        val currentSkill = getSkillById(skillId) ?: return SkillNotFound
        val modifiedSkill = block(currentSkill)

        assertNoInvalidModifications(currentSkill, modifiedSkill)

        val updatedSkill = updateSkillInDataStore(modifiedSkill)
        publishEvent(SkillUpdatedEvent(updatedSkill))
        return SuccessfullyUpdated(updatedSkill)
    }

    private fun assertNoInvalidModifications(currentSkill: Skill, modifiedSkill: Skill) {
        check(currentSkill.id == modifiedSkill.id) { "ID must not be changed!" }
        check(currentSkill.version == modifiedSkill.version) { "Version must not be changed!" }
        check(currentSkill.lastUpdate == modifiedSkill.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class UpdateSkillByIdResult {
    object SkillNotFound : UpdateSkillByIdResult()
    data class SuccessfullyUpdated(val skill: Skill) : UpdateSkillByIdResult()
}
