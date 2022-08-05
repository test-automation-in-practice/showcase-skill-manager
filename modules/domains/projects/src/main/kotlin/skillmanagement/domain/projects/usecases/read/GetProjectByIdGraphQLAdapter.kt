package skillmanagement.domain.projects.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.ProjectRepresentation
import skillmanagement.domain.projects.model.toRepresentation

@GraphQLAdapter
internal class GetProjectByIdGraphQLAdapter(
    private val delegate: GetProjectByIdFunction
) {

    @QueryMapping
    fun getProjectById(@Argument id: ProjectId): ProjectRepresentation? = delegate(id)?.toRepresentation()

}
