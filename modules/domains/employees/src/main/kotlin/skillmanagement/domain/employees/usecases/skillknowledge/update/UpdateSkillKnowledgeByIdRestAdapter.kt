package skillmanagement.domain.employees.usecases.skillknowledge.update

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
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillKnowledgeChangeData
import skillmanagement.domain.employees.model.merge
import skillmanagement.domain.employees.model.toChangeData
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.SkillKnowledgeNotChanged
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.SkillKnowledgeNotFound
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
internal class UpdateSkillKnowledgeByIdRestAdapter(
    private val updateSkillKnowledgeById: UpdateSkillKnowledgeByIdFunction,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: SkillKnowledgeChangeData
    ): ResponseEntity<EmployeeResource> =
        update(employeeId, skillId) { it.merge(request) }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<EmployeeResource> =
        update(employeeId, skillId) { it.merge(applyPatch(patch, it.toChangeData())) }

    private fun update(
        employeeId: UUID,
        skillId: UUID,
        block: (SkillKnowledge) -> SkillKnowledge
    ): ResponseEntity<EmployeeResource> = updateSkillKnowledgeById(employeeId, skillId, block)
        .map { employee -> ok(employee.toResource()) }
        .getOrHandle { failure ->
            when (failure) {
                EmployeeNotFound, SkillKnowledgeNotFound -> notFound().build()
                is SkillKnowledgeNotChanged -> ok(failure.employee.toResource())
            }
        }

}
