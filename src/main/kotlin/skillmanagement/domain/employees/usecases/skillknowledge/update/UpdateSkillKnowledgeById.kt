package skillmanagement.domain.employees.usecases.skillknowledge.update

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SuccessfullyUpdatedSkillKnowledge
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.NotUpdatedBecauseEmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdatedEmployee
import java.util.UUID

@BusinessFunction
class UpdateSkillKnowledgeById(
    private val updateEmployeeById: UpdateEmployeeById
) {

    operator fun invoke(
        employeeId: UUID,
        skillId: UUID,
        block: (SkillKnowledge) -> SkillKnowledge
    ): UpdateSkillKnowledgeResult {
        val updateResult = updateEmployeeById(employeeId) { employee ->
            val updatedSkills = employee.skills
                .map { skillKnowledge ->
                    if (skillKnowledge.skill.id == skillId) {
                        update(skillKnowledge, block)
                    } else {
                        skillKnowledge
                    }
                }
            employee.copy(skills = updatedSkills)
        }

        return when (updateResult) {
            is NotUpdatedBecauseEmployeeNotFound -> EmployeeNotFound
            is NotUpdatedBecauseEmployeeNotChanged -> SkillKnowledgeNotFound
            is SuccessfullyUpdatedEmployee -> SuccessfullyUpdatedSkillKnowledge(updateResult.employee)
        }
    }

    private fun update(
        currentKnowledge: SkillKnowledge,
        block: (SkillKnowledge) -> SkillKnowledge
    ): SkillKnowledge {
        val modifiedKnowledge = block(currentKnowledge)
        check(currentKnowledge.skill == modifiedKnowledge.skill) { "Skill must not be changed!" }
        return modifiedKnowledge
    }

}

sealed class UpdateSkillKnowledgeResult {
    object EmployeeNotFound : UpdateSkillKnowledgeResult()
    object SkillKnowledgeNotFound : UpdateSkillKnowledgeResult()
    data class SuccessfullyUpdatedSkillKnowledge(val employee: Employee) : UpdateSkillKnowledgeResult()
}
