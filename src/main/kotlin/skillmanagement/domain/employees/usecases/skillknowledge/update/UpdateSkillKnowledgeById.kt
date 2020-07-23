package skillmanagement.domain.employees.usecases.skillknowledge.update

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.usecases.get.GetEmployeeById
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.update.UpdateSkillKnowledgeResult.SuccessfullyUpdated
import skillmanagement.domain.employees.usecases.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class UpdateSkillKnowledgeById(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(
        employeeId: UUID,
        skillId: UUID,
        block: (SkillKnowledge) -> SkillKnowledge
    ): UpdateSkillKnowledgeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val currentKnowledge = employee.skills.singleOrNull { it.skill.id == skillId } ?: return SkillKnowledgeNotFound
        val modifiedKnowledge = block(currentKnowledge)

        assertNoInvalidModifications(currentKnowledge, modifiedKnowledge)

        val updatedEmployee = employee.setSkillKnowledge(modifiedKnowledge)
        updateEmployeeInDataStore(updatedEmployee)
        return SuccessfullyUpdated(modifiedKnowledge)
    }

    private fun assertNoInvalidModifications(currentKnowledge: SkillKnowledge, modifiedKnowledge: SkillKnowledge) {
        check(currentKnowledge.skill == modifiedKnowledge.skill) { "Skill must not be changed!" }
    }

}

sealed class UpdateSkillKnowledgeResult {
    object EmployeeNotFound : UpdateSkillKnowledgeResult()
    object SkillKnowledgeNotFound : UpdateSkillKnowledgeResult()
    data class SuccessfullyUpdated(val knowledge: SkillKnowledge) : UpdateSkillKnowledgeResult()
}
