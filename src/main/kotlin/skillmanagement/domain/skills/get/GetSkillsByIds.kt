package skillmanagement.domain.skills.get

import skillmanagement.domain.skills.Skill
import skillmanagement.domain.BusinessFunction
import java.util.*

@BusinessFunction
class GetSkillsByIds(
    private val getSkillFromDataStore: GetSkillFromDataStore
) {

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Skill> {
        return getSkillFromDataStore(ids)
            .map { it.id to it }
            .toMap()
    }

}

