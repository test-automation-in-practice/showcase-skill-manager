package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.domain.skills.model.Skill

@Component
class GetSkillsPageGraphQLQuery(
    private val getSkills: GetSkillsFunction
) : GraphQLQueryResolver {

    fun getSkillsPage(page: Int, size: Int): List<Skill> = withErrorHandling {
        getSkills(AllSkillsQuery(PageIndex(page), PageSize(size)))
    }

}
