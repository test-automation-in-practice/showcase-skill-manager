package skillmanagement.domain.skills.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.PublishEvent
import skillmanagement.domain.skills.SkillDeletedEvent
import skillmanagement.domain.skills.delete.DeleteSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.delete.DeleteSkillByIdResult.SuccessfullyDeleted
import skillmanagement.domain.skills.get.GetSkillById
import java.util.UUID

@BusinessFunction
class DeleteSkillById(
    private val getSkillById: GetSkillById,
    private val deleteSkillFromDataStore: DeleteSkillFromDataStore,
    private val publishEvent: PublishEvent
) {

    // TODO: Security - Only invokable by Skill-Admins
    operator fun invoke(id: UUID): DeleteSkillByIdResult {
        val skill = getSkillById(id) ?: return SkillNotFound
        deleteSkillFromDataStore(id)
        publishEvent(SkillDeletedEvent(skill))
        return SuccessfullyDeleted
    }

}

sealed class DeleteSkillByIdResult {
    object SkillNotFound : DeleteSkillByIdResult()
    object SuccessfullyDeleted : DeleteSkillByIdResult()
}
