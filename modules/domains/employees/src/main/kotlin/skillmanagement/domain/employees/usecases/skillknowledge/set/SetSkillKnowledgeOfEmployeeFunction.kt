package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.gateways.GetSkillByIdAdapterFunction
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class SetSkillKnowledgeOfEmployeeFunction internal constructor(
    private val getSkillById: GetSkillByIdAdapterFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, data: SkillKnowledgeSetData) =
        updateEmployeeById(employeeId) { employee ->
            employee.addOrUpdateSkillKnowledge(skillKnowledge(data))
        }

    private fun skillKnowledge(data: SkillKnowledgeSetData) =
        SkillKnowledge(
            skill = data.skill,
            level = data.level,
            secret = data.secret
        )

}

data class SkillKnowledgeSetData(
    val skill: SkillData,
    val level: SkillLevel,
    val secret: Boolean
)
