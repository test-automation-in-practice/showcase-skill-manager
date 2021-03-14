package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.model.Page
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill

@BusinessFunction
class GetSkillsPageFunction internal constructor(
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction,
    private val searchIndex: SearchIndex<Skill>
) {

    operator fun invoke(query: SkillsQuery): Page<Skill> {
        val page = when (query) {
            is SkillsMatchingQuery -> searchIndex.query(query)
            is AllSkillsQuery -> searchIndex.findAll(query)
        }
        val skillsMap = getSkillsFromDataStore(page.content)
        val skills = page.content.mapNotNull { skillsMap[it] }
        return page.withOtherContent(skills)
    }

}
