package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.toRepresentations

@GraphQLAdapter
internal class SearchSkillsGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun searchSkills(query: String, pagination: Pagination?): Page<SkillRepresentation> =
        getSkillsPage(SkillsMatchingQuery(query, pagination ?: Pagination.DEFAULT)).toRepresentations()

}
