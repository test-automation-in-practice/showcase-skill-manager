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
internal class SearchSkillsGraphQLAdapter(
    private val delegate: GetSkillsPageFunction
) {

    @QueryMapping
    fun searchSkills(
        @Argument query: String,
        @Argument pageIndex: PageIndex,
        @Argument pageSize: PageSize
    ): Page<SkillRepresentation> {
        val pagination = Pagination(pageIndex, pageSize)
        val skillsMatchingQuery = SkillsMatchingQuery(query, pagination)
        val result = delegate(skillsMatchingQuery)

        return result.toRepresentations()
    }

}
