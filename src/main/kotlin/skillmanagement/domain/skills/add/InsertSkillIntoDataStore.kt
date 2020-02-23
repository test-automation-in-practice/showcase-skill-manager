package skillmanagement.domain.skills.add

import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillRepository
import skillmanagement.domain.skills.toDocument
import skillmanagement.domain.TechnicalFunction

@TechnicalFunction
class InsertSkillIntoDataStore(
    private val repository: SkillRepository
) {

    operator fun invoke(skill: Skill) {
        repository.insert(skill.toDocument())
    }

}
