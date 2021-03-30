package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.Employee
import java.util.UUID

@GraphQLAdapter
internal class GetEmployeeByIdGraphQLAdapter(
    private val getEmployeeById: GetEmployeeByIdFunction
) : GraphQLQueryResolver {

    fun getEmployeeById(id: String): Employee? = getEmployeeById(UUID.fromString(id))

}
