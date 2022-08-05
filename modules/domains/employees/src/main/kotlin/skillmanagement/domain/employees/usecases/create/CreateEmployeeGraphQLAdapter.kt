package skillmanagement.domain.employees.usecases.create

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import skillmanagement.common.stereotypes.GraphQLAdapter
import skillmanagement.domain.employees.model.EmailAddress
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.JobTitle
import skillmanagement.domain.employees.model.LastName
import skillmanagement.domain.employees.model.TelephoneNumber
import skillmanagement.domain.employees.model.toRepresentation

@GraphQLAdapter
internal class CreateEmployeeGraphQLAdapter(
    private val delegate: CreateEmployeeFunction
) {

    @MutationMapping
    fun createEmployee(
        @Argument firstName: FirstName,
        @Argument lastName: LastName,
        @Argument title: JobTitle,
        @Argument email: EmailAddress,
        @Argument telephone: TelephoneNumber
    ): EmployeeRepresentation {
        val data = EmployeeCreationData(
            firstName = firstName,
            lastName = lastName,
            title = title,
            email = email,
            telephone = telephone
        )
        return delegate(data).toRepresentation()
    }

}
