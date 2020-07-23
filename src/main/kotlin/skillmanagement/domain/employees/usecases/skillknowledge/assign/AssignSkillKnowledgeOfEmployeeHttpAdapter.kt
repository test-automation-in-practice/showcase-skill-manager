package skillmanagement.domain.employees.usecases.skillknowledge.assign

import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.model.SkillKnowledgeResource
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.SuccessfullyAssigned
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/skills")
class AssignSkillKnowledgeOfEmployeeHttpAdapter(
    private val assignSkillKnowledgeOfEmployee: AssignSkillKnowledgeOfEmployee
) {

    private val log = logger {}

    @PostMapping
    fun post(
        @PathVariable employeeId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<SkillKnowledgeResource> {
        log.info { "Setting knowledge for skill [${request.skillId}] of employee [$employeeId]" }
        val result = assignSkillKnowledgeOfEmployee(
            employeeId = employeeId,
            skillId = request.skillId,
            level = request.level,
            secret = request.secret
        )
        log.info { "Result: $result" }
        return when (result) {
            EmployeeNotFound, SkillNotFound -> notFound().build()
            is SuccessfullyAssigned -> ok(result.skillKnowledge.toResource(employeeId))
        }
    }

    data class Request(
        val skillId: UUID,
        val level: SkillLevel,
        val secret: Boolean = false
    )

}
