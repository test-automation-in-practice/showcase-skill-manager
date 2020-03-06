package skillmanagement.domain.skills.delete

import skillmanagement.domain.BusinessFunction
import java.util.UUID

@BusinessFunction
class DeleteSkillById(
    private val deleteSkillFromDataStore: DeleteSkillFromDataStore
) {

    // TODO: Security - Only invokable by Skill-Admins
    operator fun invoke(id: UUID) {
        deleteSkillFromDataStore(id)
        // TODO: Remove skills from linked employees? Maybe with an event?
    }

}
