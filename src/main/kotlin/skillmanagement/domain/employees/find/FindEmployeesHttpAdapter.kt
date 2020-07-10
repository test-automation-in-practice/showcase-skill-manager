package skillmanagement.domain.employees.find

import org.springframework.hateoas.CollectionModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.toResource

@HttpAdapter
@RequestMapping("/api/employees")
class FindEmployeesHttpAdapter(
    private val findEmployees: FindEmployees
) {

    @GetMapping
    fun get(): CollectionModel<EmployeeResource> =
        findEmployees().toResource()

}
