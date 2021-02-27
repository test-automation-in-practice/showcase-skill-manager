package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import java.util.UUID

@TechnicalFunction
class GetSkillByIdAdapterFunction(
    private val getSkillById: GetSkillByIdFunction
) {
    operator fun invoke(id: UUID): SkillData? = getSkillById(id)?.toData()
    private fun Skill.toData() = SkillData(id = id, label = label.toString())
}