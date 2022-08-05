package skillmanagement.domain.employees.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.toRepresentation

@GraphQLAdapter
internal class GetEmployeeByIdGraphQLAdapter(
    private val delegate: GetEmployeeByIdFunction
) {

    @QueryMapping
    fun getEmployeeById(@Argument id: EmployeeId): EmployeeRepresentation? = delegate(id)?.toRepresentation()

}
