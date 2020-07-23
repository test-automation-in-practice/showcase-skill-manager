package skillmanagement.domain.employees.usecases.skillknowledge.update

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
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillKnowledgeResource
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SuccessfullyUpdated
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
class UpdateSkillKnowledgeByIdHttpAdapter(
    private val updateSkillKnowledgeById: UpdateSkillKnowledgeById,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: ChangeData
    ): ResponseEntity<SkillKnowledgeResource> {
        val result = updateSkillKnowledgeById(employeeId, skillId) {
            it.merge(request)
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SkillKnowledgeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.knowledge.toResource(employeeId))
        }
    }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<SkillKnowledgeResource> {
        val result = updateSkillKnowledgeById(employeeId, skillId) {
            it.merge(applyPatch(patch, it.toChangeData()))
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is SkillKnowledgeNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.knowledge.toResource(employeeId))
        }
    }

    data class ChangeData(
        val level: SkillLevel,
        val secret: Boolean
    )

    private fun SkillKnowledge.toChangeData(): ChangeData =
        ChangeData(level = level, secret = secret)

    private fun SkillKnowledge.merge(changes: ChangeData): SkillKnowledge =
        copy(level = changes.level, secret = changes.secret)

}
