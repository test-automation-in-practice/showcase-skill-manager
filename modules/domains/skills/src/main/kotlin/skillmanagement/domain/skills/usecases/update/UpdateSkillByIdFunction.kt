package skillmanagement.domain.skills.usecases.update

import arrow.core.Either
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.common.success
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.SkillUpdatedEvent
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotChanged
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotFound

@BusinessFunction
class UpdateSkillByIdFunction internal constructor(
    private val getSkillById: GetSkillByIdFunction,
    private val updateSkillInDataStore: UpdateSkillInDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    @RetryOnConcurrentSkillUpdate
    operator fun invoke(
        skillId: SkillId,
        block: (SkillEntity) -> SkillEntity
    ): Either<SkillUpdateFailure, SkillEntity> {
        val currentSkill = getSkillById(skillId) ?: return failure(SkillNotFound)
        val modifiedSkill = block(currentSkill)

        if (modifiedSkill == currentSkill) return failure(SkillNotChanged(currentSkill))
        assertNoInvalidModifications(currentSkill, modifiedSkill)

        val updatedSkill = updateSkillInDataStore(modifiedSkill)
        publishEvent(SkillUpdatedEvent(updatedSkill))
        return success(updatedSkill)
    }

    private fun assertNoInvalidModifications(currentSkill: SkillEntity, modifiedSkill: SkillEntity) {
        check(currentSkill.id == modifiedSkill.id) { "ID must not be changed!" }
        check(currentSkill.version == modifiedSkill.version) { "Version must not be changed!" }
        check(currentSkill.lastUpdate == modifiedSkill.lastUpdate) { "Last update must not be changed!" }
    }

}

sealed class SkillUpdateFailure {
    object SkillNotFound : SkillUpdateFailure()
    data class SkillNotChanged(val skill: SkillEntity) : SkillUpdateFailure()
}
