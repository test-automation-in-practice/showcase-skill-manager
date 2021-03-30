package skillmanagement.domain.employees.usecases.delete

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeByIdResult.SuccessfullyDeleted
import java.util.UUID

@GraphQLAdapter
internal class DeleteEmployeeByIdGraphQLAdapter(
    private val deleteEmployeeById: DeleteEmployeeByIdFunction
) : GraphQLMutationResolver {

    fun deleteEmployeeById(id: String): Boolean =
        when (deleteEmployeeById(UUID.fromString(id))) {
            EmployeeNotFound -> false
            SuccessfullyDeleted -> true
        }

}
