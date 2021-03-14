package skillmanagement.domain.employees.usecases.read

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.toResource
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{id}")
internal class GetEmployeeByIdRestAdapter(
    private val getEmployeeById: GetEmployeeByIdFunction
) {

    @GetMapping
    fun get(@PathVariable id: UUID): ResponseEntity<EmployeeResource> {
        val employee = getEmployeeById(id)
        if (employee != null) {
            return ok(employee.toResource())
        }
        return noContent().build()
    }

}
