package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import java.util.UUID

@BusinessFunction
class GetSkillByIdFunction internal constructor(
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction
) {

    operator fun invoke(id: UUID): Skill? {
        return getSkillsFromDataStore(id)
    }

}
