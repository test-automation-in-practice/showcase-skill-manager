package skillmanagement.domain.employees.usecases.create

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.toResource

@RestAdapter
@RequestMapping("/api/employees")
internal class CreateEmployeeRestAdapter(
    private val createEmployee: CreateEmployeeFunction
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: EmployeeCreationData): ResponseEntity<EmployeeResource> {
        val employee = createEmployee(request)
        val location = locationOf(employee)
        val resource = employee.toResource()
        return created(location).body(resource)
    }

    private fun locationOf(employee: EmployeeEntity) = fromCurrentRequest().path("/${employee.id}").build().toUri()

}
