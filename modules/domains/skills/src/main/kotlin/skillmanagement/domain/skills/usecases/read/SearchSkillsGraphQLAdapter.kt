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
internal class SearchSkillsGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun searchSkills(query: String, pagination: Pagination?): Page<Skill> = withErrorHandling {
        getSkillsPage(query(query, pagination))
    }

    private fun query(query: String, pagination: Pagination?) =
        SkillsMatchingQuery(
            queryString = query,
            pageIndex = PageIndex.of(pagination?.index),
            pageSize = PageSize.of(pagination?.size)
        )

}
