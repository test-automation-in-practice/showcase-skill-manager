package skillmanagement.domain.skills.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.Skill

@BusinessFunction
class FindSkills(
    private val findSkillsInDataStore: FindSkillsInDataStore
) {

    operator fun invoke(pageNumber: Int, pageSize: Int): Page<Skill> {
        return findSkillsInDataStore(pageNumber, pageSize)
    }

}
