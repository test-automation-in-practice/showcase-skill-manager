package skillmanagement.domain.employees.skillknowledge.delete

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SuccessfullyDeleted
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val deleteSkillKnowledgeFromDataStore: DeleteSkillKnowledgeFromDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(employeeId: UUID, skillId: UUID): DeleteSkillKnowledgeOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val knowledge = employee.skills.singleOrNull { it.skill.id == skillId } ?: return SkillKnowledgeNotFound

        deleteSkillKnowledgeFromDataStore(employee, knowledge)
        return SuccessfullyDeleted
    }

}

sealed class DeleteSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SkillKnowledgeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SuccessfullyDeleted : DeleteSkillKnowledgeOfEmployeeResult()
}
