package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill

@GraphQLAdapter
internal class GetSkillsPageGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun getSkillsPage(index: Int?, size: Int?): Page<Skill> = withErrorHandling {
        getSkillsPage(query(index, size))
    }

    private fun query(index: Int?, size: Int?) =
        AllSkillsQuery(
            pageIndex = index?.let(::PageIndex) ?: PageIndex.DEFAULT,
            pageSize = size?.let(::PageSize) ?: PageSize.DEFAULT
        )

}
