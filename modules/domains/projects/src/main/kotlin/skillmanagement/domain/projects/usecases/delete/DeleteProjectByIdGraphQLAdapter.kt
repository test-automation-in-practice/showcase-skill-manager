package skillmanagement.domain.projects.usecases.delete

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectId

@GraphQLAdapter
internal class DeleteProjectByIdGraphQLAdapter(
    private val delegate: DeleteProjectByIdFunction
) {

    @MutationMapping
    fun deleteProjectById(@Argument id: ProjectId): Boolean = delegate(id)

}
