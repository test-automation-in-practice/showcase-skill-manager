package skillmanagement.domain.skills.usecases.get

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import java.util.UUID

@BusinessFunction
class GetSkillById(
    private val getSkillFromDataStore: GetSkillFromDataStore
) {

    operator fun invoke(id: UUID): Skill? {
        return getSkillFromDataStore(id)
    }

}
