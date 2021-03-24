@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.employees.model

data class EmployeeCreationData(
    val firstName: FirstName,
    val lastName: LastName,
    val title: JobTitle,
    val email: EmailAddress,
    val telephone: TelephoneNumber
)
