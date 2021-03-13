package skillmanagement.domain.skills.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import skillmanagement.common.graphql.withErrorHandling
import skillmanagement.domain.skills.model.Skill
import java.util.UUID

@Component
internal class GetSkillByIdGraphQLQuery(
    private val getSkillById: GetSkillByIdFunction
) : GraphQLQueryResolver {

    fun getSkillById(id: String): Skill? = withErrorHandling {
        getSkillById(UUID.fromString(id))
    }

}
