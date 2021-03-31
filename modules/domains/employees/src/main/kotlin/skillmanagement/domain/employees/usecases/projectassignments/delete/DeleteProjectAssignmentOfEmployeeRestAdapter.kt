package skillmanagement.domain.employees.usecases.projectassignments.delete

import arrow.core.getOrHandle
import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeletionFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.delete.DeletionFailure.ProjectAssignmentNotFound
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
internal class DeleteProjectAssignmentOfEmployeeRestAdapter(
    private val deleteProjectAssignmentOfEmployee: DeleteProjectAssignmentOfEmployeeFunction
) {

    private val log = logger {}

    @DeleteMapping
    fun delete(@PathVariable employeeId: UUID, @PathVariable assignmentId: UUID): ResponseEntity<EmployeeResource> {
        log.info { "Deleting project assignment [$assignmentId] of employee [$employeeId]" }
        val result = deleteProjectAssignmentOfEmployee(employeeId, assignmentId)
        log.info { "Result: $result" }

        return result.map { employee -> ok(employee.toResource()) }
            .getOrHandle { failure ->
                when (failure) {
                    EmployeeNotFound, ProjectAssignmentNotFound -> noContent().build()
                }
            }
    }

}
