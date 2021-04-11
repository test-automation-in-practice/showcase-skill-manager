package skillmanagement.domain.employees.usecases.skillknowledge.update

import arrow.core.Either
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.SkillKnowledgeNotChanged
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateFailure.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction

@BusinessFunction
class UpdateSkillKnowledgeByIdFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    // TODO: extract update function to Employee

    operator fun invoke(
        employeeId: EmployeeId,
        skillId: SkillId,
        block: (SkillKnowledge) -> SkillKnowledge
    ): Either<UpdateFailure, EmployeeEntity> {
        var knowledgeExists = false
        val updateResult = updateEmployeeById(employeeId) { employee ->
            val updatedSkills = employee.skills
                .map { skillKnowledge ->
                    if (skillKnowledge.skill.id == skillId) {
                        knowledgeExists = true
                        update(skillKnowledge, block)
                    } else {
                        skillKnowledge
                    }
                }
            employee.copy(skills = updatedSkills)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> when (knowledgeExists) {
                    true -> SkillKnowledgeNotChanged(failure.employee)
                    false -> SkillKnowledgeNotFound
                }
            }
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

sealed class UpdateFailure {
    object EmployeeNotFound : UpdateFailure()
    object SkillKnowledgeNotFound : UpdateFailure()
    data class SkillKnowledgeNotChanged(val employee: EmployeeEntity) : UpdateFailure()
}
