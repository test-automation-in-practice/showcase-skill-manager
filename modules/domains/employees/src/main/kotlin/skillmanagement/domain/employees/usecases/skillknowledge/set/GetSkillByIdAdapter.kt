package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.usecases.get.GetSkillById
import java.util.UUID

@TechnicalFunction
class GetSkillByIdAdapter(
    private val getSkillById: GetSkillById
) {
    operator fun invoke(id: UUID): SkillData? = getSkillById(id)?.toData()
    private fun Skill.toData() = SkillData(id = id, label = label)
}