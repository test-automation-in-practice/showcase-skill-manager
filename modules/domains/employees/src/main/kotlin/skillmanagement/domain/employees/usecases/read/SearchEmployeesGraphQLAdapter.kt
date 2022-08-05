package skillmanagement.domain.employees.usecases.read

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import skillmanagement.common.model.Page
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.toRepresentations

@GraphQLAdapter
internal class SearchEmployeesGraphQLAdapter(
    private val delegate: GetEmployeesPageFunction
) {

    @QueryMapping
    fun searchEmployees(
        @Argument query: String,
        @Argument pageIndex: PageIndex,
        @Argument pageSize: PageSize
    ): Page<EmployeeRepresentation> {
        val pagination = Pagination(pageIndex, pageSize)
        val employeesMatchingQuery = EmployeesMatchingQuery(query, pagination)
        val result = delegate(employeesMatchingQuery)

        return result.toRepresentations()
    }

}
