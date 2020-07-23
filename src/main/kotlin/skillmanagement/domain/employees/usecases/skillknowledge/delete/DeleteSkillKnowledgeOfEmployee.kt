package skillmanagement.domain.employees.usecases.skillknowledge.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.usecases.get.GetEmployeeById
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SuccessfullyDeleted
import skillmanagement.domain.employees.usecases.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeInDataStore
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(employeeId: UUID, skillId: UUID): DeleteSkillKnowledgeOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        if (!employee.hasSkillKnowledgeBySkillId(skillId)) {
            return SkillKnowledgeNotFound
        }

        updateEmployeeInDataStore(employee.removeSkillKnowledgeBySkillId(skillId))
        return SuccessfullyDeleted
    }

}

sealed class DeleteSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SkillKnowledgeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SuccessfullyDeleted : DeleteSkillKnowledgeOfEmployeeResult()
}
