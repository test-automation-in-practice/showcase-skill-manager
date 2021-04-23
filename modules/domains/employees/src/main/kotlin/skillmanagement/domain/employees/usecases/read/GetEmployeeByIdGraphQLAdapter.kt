package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.employeeId
import skillmanagement.domain.employees.model.toRepresentation

@GraphQLAdapter
internal class GetEmployeeByIdGraphQLAdapter(
    private val getEmployeeById: GetEmployeeByIdFunction
) : GraphQLQueryResolver {

    fun getEmployeeById(id: String): EmployeeRepresentation? =
        getEmployeeById(employeeId(id))?.toRepresentation()

}
