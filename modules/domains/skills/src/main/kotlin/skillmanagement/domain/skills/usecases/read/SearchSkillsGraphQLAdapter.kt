package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill

@GraphQLAdapter
internal class SearchSkillsGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun searchSkills(query: String, index: Int?, size: Int?): Page<Skill> = withErrorHandling {
        getSkillsPage(query(query, index, size))
    }

    private fun query(query: String, index: Int?, size: Int?) =
        SkillsMatchingQuery(queryString = query, pageIndex = pageIndex(index), pageSize = pageSize(size))

    private fun pageIndex(index: Int?) = index?.let(::PageIndex) ?: PageIndex.DEFAULT
    private fun pageSize(size: Int?) = size?.let(::PageSize) ?: PageSize.DEFAULT

}
