package skillmanagement.domain.employees.usecases.projectassignments.delete

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.common.stereotypes.HttpAdapter
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
class DeleteProjectAssignmentOfEmployeeHttpAdapter(
    private val deleteProjectAssignmentOfEmployee: DeleteProjectAssignmentOfEmployee
) {

    private val log = logger {}

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable employeeId: UUID, @PathVariable assignmentId: UUID) {
        log.info { "Deleting project assignment [$assignmentId] of employee [$employeeId]" }
        val result = deleteProjectAssignmentOfEmployee(employeeId, assignmentId)
        log.info { "Result: $result" }
    }

}
