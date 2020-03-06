package skillmanagement.domain.skills.get

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.Skill
import java.util.UUID

@BusinessFunction
class GetSkillById(
    private val getSkillFromDataStore: GetSkillFromDataStore
) {

    operator fun invoke(id: UUID): Skill? {
        return getSkillFromDataStore(id)
    }

}
