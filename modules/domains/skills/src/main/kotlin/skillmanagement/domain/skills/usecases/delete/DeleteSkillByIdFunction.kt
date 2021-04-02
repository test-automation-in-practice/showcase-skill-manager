package skillmanagement.domain.skills.usecases.delete

import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction

@BusinessFunction
class DeleteSkillByIdFunction internal constructor(
    private val getSkillById: GetSkillByIdFunction,
    private val deleteSkillFromDataStore: DeleteSkillFromDataStoreFunction,
    private val publishEvent: PublishEventFunction
) {

    operator fun invoke(id: SkillId): Boolean {
        val skill = getSkillById(id) ?: return false
        deleteSkillFromDataStore(id)
        publishEvent(SkillDeletedEvent(skill))
        return true
    }

}
