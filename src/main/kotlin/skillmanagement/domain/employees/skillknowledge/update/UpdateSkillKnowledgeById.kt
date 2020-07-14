package skillmanagement.domain.employees.skillknowledge.update

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.skillknowledge.update.UpdateSkillKnowledgeResult.SuccessfullyUpdated
import skillmanagement.domain.employees.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
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
