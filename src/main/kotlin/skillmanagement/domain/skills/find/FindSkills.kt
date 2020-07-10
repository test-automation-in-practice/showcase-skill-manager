package skillmanagement.domain.skills.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.Skill

@BusinessFunction
class FindSkills(
    private val findSkillsInDataStore: FindSkillsInDataStore
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindSkillsQuery = NoOpQuery): List<Skill> {
        return findSkillsInDataStore(query)
    }

}

sealed class FindSkillsQuery
data class SkillsWithLabelLike(val searchTerms: String) : FindSkillsQuery()
object NoOpQuery : FindSkillsQuery()
