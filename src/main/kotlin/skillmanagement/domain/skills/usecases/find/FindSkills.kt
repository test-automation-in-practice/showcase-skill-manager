package skillmanagement.domain.skills.usecases.find

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.domain.skills.usecases.get.GetSkillFromDataStore

@BusinessFunction
class FindSkills(
    private val findAllSkillsInDataStore: FindAllSkillsInDataStore,
    private val getSkillFromDataStore: GetSkillFromDataStore,
    private val searchIndex: SkillSearchIndex
) {

    // TODO: Security + query parameter + pagination
    operator fun invoke(query: FindSkillsQuery = NoOpQuery): List<Skill> =
        when (query) {
            is SkillsMatchingQuery -> {
                val ids = searchIndex.query(query.queryString)
                getSkillFromDataStore(ids).values.toList()
            }
            NoOpQuery -> findAllSkillsInDataStore()
        }

}

sealed class FindSkillsQuery
data class SkillsMatchingQuery(val queryString: String) : FindSkillsQuery()
object NoOpQuery : FindSkillsQuery()
