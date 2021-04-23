package skillmanagement.domain.employees.usecases.skillknowledge.set

import arrow.core.getOrHandle
import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeRepresentation
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.model.toResource

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/skills")
internal class SetSkillKnowledgeOfEmployeeRestAdapter(
    private val getSkillById: GetSkillByIdAdapterFunction,
    private val setSkillKnowledgeOfEmployee: SetSkillKnowledgeOfEmployeeFunction
) {

    private val log = logger {}

    @PostMapping
    fun post(
        @PathVariable employeeId: EmployeeId,
        @RequestBody request: Request
    ): ResponseEntity<EmployeeRepresentation> {
        val skillId = request.skillId
        log.info { "Setting knowledge for skill [$skillId] of employee [$employeeId]" }

        val skill = getSkillById(skillId)
        if (skill == null) {
            log.debug { "Skill [$skillId] not found!" }
            return notFound().build()
        }
        val data = request.toSetData(skill)

        return setSkillKnowledgeOfEmployee(employeeId, data)
            .map { employee -> ok(employee.toResource()) }
            .getOrHandle { failure ->
                log.debug { "Employee update failed: $failure" }
                notFound().build()
            }
    }

    private fun Request.toSetData(skill: SkillData) =
        SkillKnowledgeSetData(
            skill = skill,
            level = level,
            secret = secret
        )

    data class Request(
        val skillId: SkillId,
        val level: SkillLevel,
        val secret: Boolean = false
    )

}
