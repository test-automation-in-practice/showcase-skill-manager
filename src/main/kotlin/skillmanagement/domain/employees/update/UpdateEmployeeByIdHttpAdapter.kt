package skillmanagement.domain.employees.update

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.ApplyPatch
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.EmailAddress
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeResource
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.TelephoneNumber
import skillmanagement.domain.employees.Title
import skillmanagement.domain.employees.toResource
import skillmanagement.domain.employees.update.UpdateEmplyeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.update.UpdateEmplyeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}")
class UpdateEmployeeByIdHttpAdapter(
    private val updateEmployeeById: UpdateEmployeeById,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @RequestBody request: ChangeData
    ): ResponseEntity<EmployeeResource> {
        val result = updateEmployeeById(employeeId) {
            it.merge(request)
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.employee.toResource())
        }
    }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable employeeId: UUID,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<EmployeeResource> {
        val result = updateEmployeeById(employeeId) {
            it.merge(applyPatch(patch, it.toChangeData()))
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.employee.toResource())
        }
    }

    data class ChangeData(
        val firstName: FirstName,
        val lastName: LastName,
        val title: Title,
        val email: EmailAddress,
        val telephone: TelephoneNumber
    )

    private fun Employee.toChangeData(): ChangeData =
        ChangeData(
            firstName = firstName,
            lastName = lastName,
            title = title,
            email = email,
            telephone = telephone
        )

    private fun Employee.merge(changes: ChangeData): Employee =
        copy(
            firstName = changes.firstName,
            lastName = changes.lastName,
            title = changes.title,
            email = changes.email,
            telephone = changes.telephone
        )

}
