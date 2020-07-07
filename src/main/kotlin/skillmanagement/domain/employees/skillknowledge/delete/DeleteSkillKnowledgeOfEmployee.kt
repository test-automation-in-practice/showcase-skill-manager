package skillmanagement.domain.employees.skillknowledge.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SuccessfullyDeleted
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(employeeId: UUID, skillId: UUID): DeleteSkillKnowledgeOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        if (!employee.hasSkillKnowledge(skillId)) {
            return SkillKnowledgeNotFound
        }

        updateEmployeeInDataStore(employee.removeSkillKnowledge(skillId))
        return SuccessfullyDeleted
    }

    private fun Employee.hasSkillKnowledge(skillId: UUID): Boolean =
        skills.any { it.skill.id == skillId }

    private fun Employee.removeSkillKnowledge(skillId: UUID): Employee =
        copy(skills = skills.filter { it.skill.id != skillId })

}

sealed class DeleteSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SkillKnowledgeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SuccessfullyDeleted : DeleteSkillKnowledgeOfEmployeeResult()
}
