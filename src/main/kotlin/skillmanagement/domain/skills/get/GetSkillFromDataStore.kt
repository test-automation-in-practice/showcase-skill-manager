package skillmanagement.domain.skills.get

import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillDocument
import skillmanagement.domain.skills.SkillRepository
import skillmanagement.domain.skills.toSkill
import skillmanagement.domain.TechnicalFunction
import java.util.*

@TechnicalFunction
class GetSkillFromDataStore(
    private val repository: SkillRepository
) {

    operator fun invoke(id: UUID): Skill? = repository.findById(id)
        .map(SkillDocument::toSkill)
        .orElse(null)

    operator fun invoke(ids: Collection<UUID>): Set<Skill> = repository.findAllById(ids)
        .map(SkillDocument::toSkill)
        .toSet()

}
