package skillmanagement.domain.skills.usecases.delete

import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.skills.model.SkillId

@RestAdapter
@RequestMapping("/api/skills/{id}")
internal class DeleteSkillByIdRestAdapter(
    private val deleteSkillById: DeleteSkillByIdFunction
) {

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: SkillId) {
        deleteSkillById(id)
    }

}
