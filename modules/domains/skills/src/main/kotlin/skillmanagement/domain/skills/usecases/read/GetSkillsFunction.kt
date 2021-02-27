package skillmanagement.domain.skills.usecases.read

import skillmanagement.common.search.Page
import skillmanagement.common.search.PageIndex
import skillmanagement.common.search.PageSize
import skillmanagement.common.search.PagedFindAllQuery
import skillmanagement.common.search.PagedStringQuery
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.searchindex.SkillSearchIndex

@BusinessFunction
class GetSkillsFunction(
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction,
    private val searchIndex: SkillSearchIndex
) {

    // TODO: Security + query parameter + pagination
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
