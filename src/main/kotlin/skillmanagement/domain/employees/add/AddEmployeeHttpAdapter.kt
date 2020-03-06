package skillmanagement.domain.employees.add

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.EmailAddress
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.TelephoneNumber
import skillmanagement.domain.employees.Title
import skillmanagement.domain.employees.toResource

@HttpAdapter
@RequestMapping("/api/employees")
class AddEmployeeHttpAdapter(
    private val addEmployee: AddEmployee
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: Request): EmployeeResource {
        val employee = addEmployee(
            firstName = request.firstName,
            lastName = request.lastName,
            title = request.title,
            email = request.email,
            telephone = request.telephone
        )
        return employee.toResource()
    }

    data class Request(
        val firstName: FirstName,
        val lastName: LastName,
        val title: Title,
        val email: EmailAddress,
        val telephone: TelephoneNumber
    )

}
