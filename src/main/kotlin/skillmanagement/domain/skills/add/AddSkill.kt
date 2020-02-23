package skillmanagement.domain.skills.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.BusinessFunction

@BusinessFunction
class AddSkill(
    private val idGenerator: IdGenerator,
    private val insertSkillIntoDataStore: InsertSkillIntoDataStore
) {

    // TODO: Security - Only invokable by Skill-Admins
    operator fun invoke(label: SkillLabel): Skill {
        val skill = Skill(
            id = idGenerator.generateId(),
            label = label
        )
        insertSkillIntoDataStore(skill)
        return skill
    }

}
