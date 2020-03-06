package skillmanagement.domain.employees.skillknowledge.set

import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.SkillAssignmentResource
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.toResources
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
class SetSkillKnowledgeOfEmployeeHttpAdapter(
    private val setSkillKnowledgeOfEmployee: SetSkillKnowledgeOfEmployee
) {

    private val log = logger {}

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<SkillAssignmentResource> {
        log.info { "Setting knowledge for skill [$skillId] of employee [$employeeId]" }
        val result = setSkillKnowledgeOfEmployee(
            employeeId = employeeId,
            skillId = skillId,
            level = request.level,
            secret = request.secret
        )
        log.info { "Result: $result" }
        return when (result) {
            EmployeeNotFound, SkillNotFound -> notFound().build()
            is SuccessfullyAssigned -> ok(result.skillKnowledge.toResources(employeeId))
        }
    }

    data class Request(
        val level: SkillLevel,
        val secret: Boolean = false
    )

}
