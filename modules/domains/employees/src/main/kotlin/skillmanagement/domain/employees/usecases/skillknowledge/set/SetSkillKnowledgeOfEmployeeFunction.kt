package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction

@BusinessFunction
class SetSkillKnowledgeOfEmployeeFunction internal constructor(
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction
) {

    operator fun invoke(employeeId: EmployeeId, data: SkillKnowledgeSetData) =
        updateEmployeeEntityById(employeeId) { employee ->
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
