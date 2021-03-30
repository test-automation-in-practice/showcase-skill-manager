package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.Employee

@GraphQLAdapter
internal class GetEmployeesPageGraphQLAdapter(
    private val getEmployeesPage: GetEmployeesPageFunction
) : GraphQLQueryResolver {

    fun getEmployeesPage(pagination: Pagination?): Page<Employee> =
        getEmployeesPage(AllEmployeesQuery(pagination ?: Pagination.DEFAULT))

}
