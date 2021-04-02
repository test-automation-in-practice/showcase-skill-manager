package skillmanagement.domain.employees.usecases.skillknowledge.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, skillId: UUID) =
        updateEmployeeById(employeeId) { employee ->
            employee.removeSkillKnowledge { it.skill.id == skillId }
        }

}
