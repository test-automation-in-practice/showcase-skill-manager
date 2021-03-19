package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.Pagination
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

    fun getSkillsPage(pagination: Pagination?): Page<Skill> = withErrorHandling {
        getSkillsPage(query(pagination))
    }

    private fun query(pagination: Pagination?) =
        AllSkillsQuery(
            pageIndex = pagination?.index?.let(::PageIndex) ?: PageIndex.DEFAULT,
            pageSize = pagination?.size?.let(::PageSize) ?: PageSize.DEFAULT
        )

}
