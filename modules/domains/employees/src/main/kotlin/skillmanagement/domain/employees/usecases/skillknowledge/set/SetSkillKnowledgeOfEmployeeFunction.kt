package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.gateways.GetSkillByIdAdapterFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.usecases.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class SetSkillKnowledgeOfEmployeeFunction internal constructor(
    private val getSkillById: GetSkillByIdAdapterFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(
        employeeId: UUID,
        skillId: UUID,
        level: SkillLevel,
        secret: Boolean
    ): SetSkillKnowledgeOfEmployeeResult {
        val skill = getSkillById(skillId) ?: return SkillNotFound
        val updateResult = updateEmployeeById(employeeId) { employee ->
            val skillKnowledge = SkillKnowledge(
                skill = skill,
                level = level,
                secret = secret
            )
            employee.setSkillKnowledge(skillKnowledge)
        }

        return when (updateResult) {
            is UpdateEmployeeByIdResult.EmployeeNotFound -> EmployeeNotFound
            is EmployeeNotChanged -> error("should not happen")
            is SuccessfullyUpdated -> SuccessfullyAssigned(updateResult.employee)
        }
    }

    private fun Employee.setSkillKnowledge(skillKnowledge: SkillKnowledge): Employee =
        copy(skills = skills.filter { it.skill.id != skillKnowledge.skill.id } + skillKnowledge)

}

sealed class SetSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : SetSkillKnowledgeOfEmployeeResult()
    object SkillNotFound : SetSkillKnowledgeOfEmployeeResult()
    data class SuccessfullyAssigned(val employee: Employee) : SetSkillKnowledgeOfEmployeeResult()
}
