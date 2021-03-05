package skillmanagement.domain.employees.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.stereotypes.HttpAdapter
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.toAllResource

@HttpAdapter
@RequestMapping("/api/employees")
internal class GetEmployeesHttpAdapter(
    private val getEmployees: GetEmployeesFunction
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): PagedModel<EmployeeResource> {
        val employees = getEmployees(AllEmployeesQuery(PageIndex(page), PageSize(size)))
        return employees.toAllResource()
    }

}
