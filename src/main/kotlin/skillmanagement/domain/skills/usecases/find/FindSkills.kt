package skillmanagement.domain.skills.usecases.find

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.skills.model.Skill

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
