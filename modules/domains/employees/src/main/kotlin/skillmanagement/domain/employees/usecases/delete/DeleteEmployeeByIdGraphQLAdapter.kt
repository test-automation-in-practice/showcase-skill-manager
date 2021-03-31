package skillmanagement.domain.employees.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import java.util.UUID

@GraphQLAdapter
internal class DeleteEmployeeByIdGraphQLAdapter(
    private val deleteEmployeeById: DeleteEmployeeByIdFunction
) : GraphQLMutationResolver {

    fun deleteEmployeeById(id: String): Boolean = deleteEmployeeById(UUID.fromString(id))

}
