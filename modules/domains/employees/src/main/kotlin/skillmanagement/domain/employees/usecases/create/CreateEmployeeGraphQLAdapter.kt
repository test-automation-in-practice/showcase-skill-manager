package skillmanagement.domain.employees.usecases.create

import graphql.kickstart.tools.GraphQLMutationResolver
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.toRepresentation

@GraphQLAdapter
internal class CreateEmployeeGraphQLAdapter(
    private val createEmployeeFunction: CreateEmployeeFunction
) : GraphQLMutationResolver {

    fun createEmployee(input: EmployeeCreationData): EmployeeRepresentation =
        createEmployeeFunction(input).toRepresentation()

}
