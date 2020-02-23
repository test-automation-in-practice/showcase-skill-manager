package skillmanagement.domain.employees.add

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.toResource

@HttpAdapter
@RequestMapping("/api/employees")
class AddEmployeeHttpAdapter(
    private val addEmployee: AddEmployee
) {

    @PostMapping
    fun post(@RequestBody request: Request): EmployeeResource {
        val employee = addEmployee(
            firstName = request.firstName,
            lastName = request.lastName
        )
        return employee.toResource()
    }

    data class Request(
        val firstName: FirstName,
        val lastName: LastName
    )

}
