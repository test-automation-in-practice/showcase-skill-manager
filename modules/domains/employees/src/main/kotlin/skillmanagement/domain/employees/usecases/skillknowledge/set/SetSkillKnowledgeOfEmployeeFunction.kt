package skillmanagement.domain.employees.usecases.skillknowledge.set

import arrow.core.Either
import skillmanagement.common.failure
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.gateways.GetSkillByIdAdapterFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.usecases.skillknowledge.set.SettingFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.set.SettingFailure.SkillNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
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
    ): Either<SettingFailure, Employee> {
        val skill = getSkillById(skillId) ?: return failure(SkillNotFound)
        val updateResult = updateEmployeeById(employeeId) { employee ->
            val skillKnowledge = SkillKnowledge(
                skill = skill,
                level = level,
                secret = secret
            )
            employee.setSkillKnowledge(skillKnowledge)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> error("should not happen")
            }
        }
    }

    private fun Employee.setSkillKnowledge(skillKnowledge: SkillKnowledge): Employee =
        copy(skills = skills.filter { it.skill.id != skillKnowledge.skill.id } + skillKnowledge)

}

sealed class SettingFailure {
    object EmployeeNotFound : SettingFailure()
    object SkillNotFound : SettingFailure()
}
