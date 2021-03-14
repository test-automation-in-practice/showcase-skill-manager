package skillmanagement.domain.employees.usecases.read

import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import skillmanagement.common.searchindices.PageIndex
import skillmanagement.common.searchindices.PageSize
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.toAllResource

@RestAdapter
@RequestMapping("/api/employees")
internal class GetEmployeesPageRestAdapter(
    private val getEmployeesPage: GetEmployeesPageFunction
) {

    @GetMapping
    fun get(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): PagedModel<EmployeeResource> {
        val employees = getEmployeesPage(AllEmployeesQuery(PageIndex(page), PageSize(size)))
        return employees.toAllResource()
    }

}
