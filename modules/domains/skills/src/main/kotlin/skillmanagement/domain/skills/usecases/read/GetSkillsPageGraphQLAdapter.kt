package skillmanagement.domain.skills.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.skills.model.SkillRepresentation
import skillmanagement.domain.skills.model.toRepresentations

@GraphQLAdapter
internal class GetSkillsPageGraphQLAdapter(
    private val delegate: GetSkillsPageFunction
) {

    @QueryMapping
    fun getSkillsPage(@Argument pageIndex: PageIndex, @Argument pageSize: PageSize): Page<SkillRepresentation> {
        val pagination = Pagination(pageIndex, pageSize)
        val allSkillsQuery = AllSkillsQuery(pagination)
        val result = delegate(allSkillsQuery)

        return result.toRepresentations()
    }

}
