package skillmanagement.domain.employees.get

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.projects
import skillmanagement.domain.employees.skills
import skillmanagement.domain.employees.toResource
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{id}")
class GetEmployeeByIdHttpAdapter(
    private val getEmployeeById: GetEmployeeById
) {

    @GetMapping
    fun get(@PathVariable id: UUID, @RequestParam expand: Set<String>?): ResponseEntity<EmployeeResource> {
        val employee = getEmployeeById(id, includeSkills = expand.skills, includeProjects = expand.projects)
        if (employee != null) {
            return ok(employee.toResource())
        }
        return noContent().build()
    }

}
