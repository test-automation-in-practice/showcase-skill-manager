package skillmanagement.domain.employees.usecases.skillknowledge.delete

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
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure.EmployeeNotFound

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
internal class DeleteSkillKnowledgeOfEmployeeRestAdapter(
    private val deleteSkillKnowledgeOfEmployee: DeleteSkillKnowledgeOfEmployeeFunction
) {

    private val log = logger {}

    @DeleteMapping
    fun delete(
        @PathVariable employeeId: EmployeeId,
        @PathVariable skillId: SkillId
    ): ResponseEntity<EmployeeResource> {
        log.info { "Deleting knowledge of skill [$skillId] of employee [$employeeId]" }
        return deleteSkillKnowledgeOfEmployee(employeeId, skillId)
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
