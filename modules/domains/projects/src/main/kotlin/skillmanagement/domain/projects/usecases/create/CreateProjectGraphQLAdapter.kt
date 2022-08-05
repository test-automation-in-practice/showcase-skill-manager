package skillmanagement.domain.projects.usecases.create

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.model.ProjectRepresentation
import skillmanagement.domain.projects.model.toRepresentation

@GraphQLAdapter
internal class CreateProjectGraphQLAdapter(
    private val delegate: CreateProjectFunction
) {

    @MutationMapping
    fun createProject(@Argument label: ProjectLabel, @Argument description: ProjectDescription): ProjectRepresentation {
        val data = ProjectCreationData(
            label = label,
            description = description
        )
        return delegate(data).toRepresentation()
    }

}
