package skillmanagement.domain.skills.get

import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillDocument
import skillmanagement.domain.skills.SkillRepository
import skillmanagement.domain.skills.toSkill
import java.util.*

@TechnicalFunction
class GetSkillFromDataStore(
    private val repository: SkillRepository
) {

    operator fun invoke(id: UUID): Skill? = repository.findById(id)
        .map(SkillDocument::toSkill)
        .orElse(null)

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Skill> = repository.findAllById(ids)
        .map(SkillDocument::toSkill)
        .map { it.id to it }
        .toMap()

}
