package skillmanagement.domain.employees.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeCreationData

@GraphQLAdapter
internal class CreateEmployeeGraphQLAdapter(
    private val createEmployeeFunction: CreateEmployeeFunction
) : GraphQLMutationResolver {

    fun createEmployee(input: EmployeeCreationData): Employee = createEmployeeFunction(input)

}
