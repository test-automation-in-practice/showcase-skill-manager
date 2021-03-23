package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.graphql.Pagination
import skillmanagement.common.model.Page
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill

@GraphQLAdapter
internal class GetSkillsPageGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun getSkillsPage(pagination: Pagination?): Page<Skill> =
        getSkillsPage(query(pagination ?: Pagination.DEFAULT))

    private fun query(pagination: Pagination) = AllSkillsQuery(pagination.index, pagination.size)

}
