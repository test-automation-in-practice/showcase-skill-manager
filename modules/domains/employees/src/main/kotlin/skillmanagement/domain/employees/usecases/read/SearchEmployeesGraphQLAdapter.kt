package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeEntity

@GraphQLAdapter
internal class SearchEmployeesGraphQLAdapter(
    private val getEmployeesPage: GetEmployeesPageFunction
) : GraphQLQueryResolver {

    fun searchEmployees(query: String, pagination: Pagination?): Page<EmployeeEntity> =
        getEmployeesPage(EmployeesMatchingQuery(query, pagination ?: Pagination.DEFAULT))

}
