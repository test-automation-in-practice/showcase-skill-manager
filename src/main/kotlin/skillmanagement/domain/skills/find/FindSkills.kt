package skillmanagement.domain.skills.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.Skill

@BusinessFunction
class FindSkills(
    private val findSkillsInDataStore: FindSkillsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(): List<Skill> {
        return findSkillsInDataStore()
    }

}
