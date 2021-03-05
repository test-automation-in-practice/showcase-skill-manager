package skillmanagement.domain.employees.usecases.create

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.employees.model.EmailAddress
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.JobTitle
import skillmanagement.domain.employees.model.LastName
import skillmanagement.domain.employees.model.TelephoneNumber
import skillmanagement.domain.employees.model.toResource

@HttpAdapter
@RequestMapping("/api/employees")
internal class CreateEmployeeHttpAdapter(
    private val createEmployee: CreateEmployeeFunction
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: Request): ResponseEntity<EmployeeResource> {
        val employee = createEmployee(
            firstName = request.firstName,
            lastName = request.lastName,
            title = request.title,
            email = request.email,
            telephone = request.telephone
        )
        val location = locationOf(employee)
        val resource = employee.toResource()
        return created(location).body(resource)
    }

    private fun locationOf(employee: Employee) = fromCurrentRequest().path("/${employee.id}").build().toUri()

    data class Request(
        val firstName: FirstName,
        val lastName: LastName,
        val title: JobTitle,
        val email: EmailAddress,
        val telephone: TelephoneNumber
    )

}
