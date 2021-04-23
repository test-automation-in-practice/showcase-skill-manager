package skillmanagement.domain.employees.usecases.read

import graphql.kickstart.tools.GraphQLQueryResolver
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.toRepresentations

@GraphQLAdapter
internal class SearchEmployeesGraphQLAdapter(
    private val getEmployeesPage: GetEmployeesPageFunction
) : GraphQLQueryResolver {

    fun searchEmployees(query: String, pagination: Pagination?): Page<EmployeeRepresentation> =
        getEmployeesPage(EmployeesMatchingQuery(query, pagination ?: Pagination.DEFAULT)).toRepresentations()

}
