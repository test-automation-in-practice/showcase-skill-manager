package skillmanagement.domain.employees.usecases.delete

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeId

@GraphQLAdapter
internal class DeleteEmployeeByIdGraphQLAdapter(
    private val delegate: DeleteEmployeeByIdFunction
) {

    @MutationMapping
    fun deleteEmployeeById(@Argument id: EmployeeId): Boolean = delegate(id)

}
