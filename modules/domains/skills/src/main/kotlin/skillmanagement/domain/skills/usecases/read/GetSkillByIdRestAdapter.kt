package skillmanagement.domain.skills.usecases.read

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.toResource
import java.util.UUID

@RestAdapter
@RequestMapping("/api/skills/{id}")
internal class GetSkillByIdRestAdapter(
    private val getSkillById: GetSkillByIdFunction
) {

    @GetMapping
    fun get(@PathVariable id: UUID): ResponseEntity<SkillResource> {
        val skill = getSkillById(id)
        if (skill != null) {
            return ok(skill.toResource())
        }
        return noContent().build()
    }

}
