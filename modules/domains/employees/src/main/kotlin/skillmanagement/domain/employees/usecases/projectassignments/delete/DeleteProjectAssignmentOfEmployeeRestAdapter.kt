package skillmanagement.domain.employees.usecases.projectassignments.delete

import arrow.core.getOrHandle
import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.ProjectAssignmentId
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotFound

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
internal class DeleteProjectAssignmentOfEmployeeRestAdapter(
    private val deleteProjectAssignmentOfEmployee: DeleteProjectAssignmentOfEmployeeFunction
) {

    private val log = logger {}

    @DeleteMapping
    fun delete(
        @PathVariable employeeId: EmployeeId,
        @PathVariable assignmentId: ProjectAssignmentId
    ): ResponseEntity<EmployeeRepresentation> {
        log.info { "Deleting project assignment [$assignmentId] of employee [$employeeId]" }
        return deleteProjectAssignmentOfEmployee(employeeId, assignmentId)
            .map { employee -> ok(employee.toResource()) }
            .getOrHandle { failure ->
                log.debug { "Employee update failed: $failure" }
                when (failure) {
                    is EmployeeNotFound -> notFound().build()
                    is EmployeeNotChanged -> ok(failure.employee.toResource())
                }
            }
    }

}
