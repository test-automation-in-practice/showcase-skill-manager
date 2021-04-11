package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId

@BusinessFunction
class GetSkillByIdFunction internal constructor(
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction
) {

    operator fun invoke(id: SkillId): SkillEntity? = getSkillsFromDataStore(id)

}
