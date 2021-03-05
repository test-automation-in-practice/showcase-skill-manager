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
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdatedEmployee
import java.util.UUID

@BusinessFunction
class SetSkillKnowledgeOfEmployeeFunction(
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
            is NotUpdatedBecauseEmployeeNotFound -> EmployeeNotFound
            is NotUpdatedBecauseEmployeeNotChanged -> error("should not happen")
            is SuccessfullyUpdatedEmployee -> SuccessfullyAssigned(updateResult.employee)
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
