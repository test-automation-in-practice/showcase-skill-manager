package skillmanagement.domain.employees.find

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.projects
import skillmanagement.domain.employees.skills
import skillmanagement.domain.employees.toResource

@HttpAdapter
@RequestMapping("/api/employees")
class FindEmployeesHttpAdapter(
    private val findEmployees: FindEmployees
) {

    @GetMapping
    fun get(@RequestParam expand: Set<String>?): CollectionModel<EmployeeResource> {
        val employees = findEmployees(includeSkills = expand.skills, includeProjects = expand.projects)

        val content = employees.map(Employee::toResource)
        val selfLink = linkToCurrentMapping().slash("api/employees").withSelfRel()

        return CollectionModel.of(content, selfLink)
    }

}
