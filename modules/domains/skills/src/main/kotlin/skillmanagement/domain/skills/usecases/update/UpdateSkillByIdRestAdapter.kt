package skillmanagement.domain.skills.usecases.update

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
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillChangeData
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.merge
import skillmanagement.domain.skills.model.toChangeData
import skillmanagement.domain.skills.model.toResource
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotChanged
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotFound
import java.util.UUID

@RestAdapter
@RequestMapping("/api/skills/{id}")
internal class UpdateSkillByIdRestAdapter(
    private val updateSkillById: UpdateSkillByIdFunction,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(@PathVariable id: UUID, @RequestBody changeData: SkillChangeData): ResponseEntity<SkillResource> =
        update(id) { skill -> skill.merge(changeData) }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(@PathVariable id: UUID, @RequestBody patch: JsonPatch): ResponseEntity<SkillResource> =
        update(id) { skill -> skill.merge(applyPatch(patch, skill.toChangeData())) }

    private fun update(id: UUID, block: (Skill) -> Skill) = updateSkillById(id, block)
        .map { skill -> ok(skill.toResource()) }
        .getOrHandle { failure ->
            when (failure) {
                is SkillNotFound -> notFound().build()
                is SkillNotChanged -> ok(failure.skill.toResource())
            }
        }

}
