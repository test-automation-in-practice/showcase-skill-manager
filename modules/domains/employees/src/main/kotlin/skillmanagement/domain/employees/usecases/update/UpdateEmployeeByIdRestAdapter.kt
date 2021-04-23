package skillmanagement.domain.employees.usecases.update

import arrow.core.getOrHandle
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.http.patch.ApplyPatch
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeChangeData
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.merge
import skillmanagement.domain.employees.model.toChangeData
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotFound

@RestAdapter
@RequestMapping("/api/employees/{id}")
internal class UpdateEmployeeByIdRestAdapter(
    private val updateEmployeeById: UpdateEmployeeByIdFunction,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(@PathVariable id: EmployeeId, @RequestBody request: EmployeeChangeData): ResponseEntity<EmployeeRepresentation> =
        update(id) { employee -> employee.merge(request) }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(@PathVariable id: EmployeeId, @RequestBody patch: JsonPatch): ResponseEntity<EmployeeRepresentation> =
        update(id) { employee -> employee.merge(applyPatch(patch, employee.toChangeData())) }

    private fun update(id: EmployeeId, block: (EmployeeEntity) -> EmployeeEntity) = updateEmployeeById(id, block)
        .map { employee -> ok(employee.toResource()) }
        .getOrHandle { failure ->
            when (failure) {
                is EmployeeNotFound -> notFound().build()
                is EmployeeNotChanged -> ok(failure.employee.toResource())
            }
        }

}
