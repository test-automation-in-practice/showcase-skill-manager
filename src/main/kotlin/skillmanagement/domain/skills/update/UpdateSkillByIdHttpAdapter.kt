package skillmanagement.domain.skills.update

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
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.SkillResource
import skillmanagement.domain.skills.Tag
import skillmanagement.domain.skills.toResource
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SkillNotFound
import skillmanagement.domain.skills.update.UpdateSkillByIdResult.SuccessfullyUpdated
import java.util.SortedSet
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/skills/{skillId}")
class UpdateSkillByIdHttpAdapter(
    private val updateSkillById: UpdateSkillById,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable skillId: UUID,
        @RequestBody request: ChangeData
    ): ResponseEntity<SkillResource> {
        val result = updateSkillById(skillId) {
            it.merge(request)
        }
        return when (result) {
            SkillNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.skill.toResource())
        }
    }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable skillId: UUID,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<SkillResource> {
        val result = updateSkillById(skillId) {
            it.merge(applyPatch(patch, it.toChangeData()))
        }
        return when (result) {
            SkillNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.skill.toResource())
        }
    }

    data class ChangeData(
        val label: SkillLabel,
        val tags: SortedSet<Tag>
    )

    private fun Skill.toChangeData(): ChangeData = ChangeData(label = label, tags = tags)
    private fun Skill.merge(changes: ChangeData): Skill = copy(label = changes.label, tags = changes.tags)

}
