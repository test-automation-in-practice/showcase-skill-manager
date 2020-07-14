package skillmanagement.domain.skills.update

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.toResource
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SuccessfullyUpdated
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/skills/{skillId}")
class UpdateSkillByIdHttpAdapter(
    private val updateSkillById: UpdateSkillById
) {

    @PutMapping
    fun put(
        @PathVariable skillId: UUID,
        @RequestBody request: PutRequest
    ): ResponseEntity<SkillResource> {
        val result = updateSkillById(skillId) {
            it.copy(label = request.label)
        }
        return when (result) {
            SkillNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.skill.toResource())
        }
    }

    @PatchMapping
    fun patch(
        @PathVariable skillId: UUID,
        @RequestBody request: PatchRequest
    ): ResponseEntity<SkillResource> {
        val result = updateSkillById(skillId) {
            it.copy(label = request.label ?: it.label)
        }
        return when (result) {
            SkillNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.skill.toResource())
        }
    }

    data class PutRequest(
        val label: SkillLabel
    )

    data class PatchRequest(
        val label: SkillLabel?
    )

}
