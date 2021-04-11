package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.toRepresentations

@GraphQLAdapter
internal class GetSkillsPageGraphQLAdapter(
    private val getSkillsPage: GetSkillsPageFunction
) : GraphQLQueryResolver {

    fun getSkillsPage(pagination: Pagination?): Page<SkillRepresentation> =
        getSkillsPage(AllSkillsQuery(pagination ?: Pagination.DEFAULT)).toRepresentations()

}
