package skillmanagement.domain.employees.skillknowledge.update

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.SkillKnowledgeResource
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.SuccessfullyUpdated
import skillmanagement.domain.employees.toResource
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
class UpdateSkillKnowledgeByIdHttpAdapter(
    private val updateSkillKnowledgeById: UpdateSkillKnowledgeById
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: PutRequest
    ): ResponseEntity<SkillKnowledgeResource> {
        val result = updateSkillKnowledgeById(employeeId, skillId) {
            it.copy(
                level = request.level,
                secret = request.secret
            )
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SkillKnowledgeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.knowledge.toResource(employeeId))
        }
    }

    @PatchMapping
    fun patch(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: PatchRequest
    ): ResponseEntity<SkillKnowledgeResource> {
        val result = updateSkillKnowledgeById(employeeId, skillId) {
            it.copy(
                level = request.level ?: it.level,
                secret = request.secret ?: it.secret
            )
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SkillKnowledgeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.knowledge.toResource(employeeId))
        }
    }

    data class PutRequest(
        val level: SkillLevel,
        val secret: Boolean
    )

    data class PatchRequest(
        val level: SkillLevel?,
        val secret: Boolean?
    )

}
