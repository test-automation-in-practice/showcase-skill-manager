package skillmanagement.domain.skills.get

import skillmanagement.domain.skills.Skill
import skillmanagement.domain.BusinessFunction
import java.util.*

@BusinessFunction
class GetSkillById(
    private val getSkillFromDataStore: GetSkillFromDataStore
) {

    operator fun invoke(id: UUID): Skill? {
        return getSkillFromDataStore(id)
    }

}
