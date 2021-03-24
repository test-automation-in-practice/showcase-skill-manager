package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.Skill

@GraphQLAdapter
internal class SearchSkillsGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun searchSkills(query: String, pagination: Pagination?): Page<Skill> =
        getSkillsPage(SkillsMatchingQuery(query, pagination ?: Pagination.DEFAULT))

}
