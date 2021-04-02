package skillmanagement.domain.employees.usecases.skillknowledge.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction

@BusinessFunction
class DeleteSkillKnowledgeOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: EmployeeId, skillId: SkillId) =
        updateEmployeeById(employeeId) { employee ->
            employee.removeSkillKnowledge { it.skill.id == skillId }
        }

}
