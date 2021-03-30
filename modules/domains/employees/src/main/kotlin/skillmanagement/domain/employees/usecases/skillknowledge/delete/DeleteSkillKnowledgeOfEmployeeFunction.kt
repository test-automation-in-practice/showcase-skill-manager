package skillmanagement.domain.employees.usecases.skillknowledge.delete

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeleteSkillKnowledgeOfEmployeeResult.SuccessfullyDeletedSkillKnowledge
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, skillId: UUID): DeleteSkillKnowledgeOfEmployeeResult {
        val updateResult = updateEmployeeById(employeeId) {
            it.removeSkillKnowledgeBySkillId(skillId)
        }
        return when (updateResult) {
            is UpdateEmployeeByIdResult.EmployeeNotFound -> EmployeeNotFound
            is EmployeeNotChanged -> SkillKnowledgeNotFound
            is SuccessfullyUpdated -> SuccessfullyDeletedSkillKnowledge(updateResult.employee)
        }
    }

}

sealed class DeleteSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    object SkillKnowledgeNotFound : DeleteSkillKnowledgeOfEmployeeResult()
    data class SuccessfullyDeletedSkillKnowledge(val employee: Employee) : DeleteSkillKnowledgeOfEmployeeResult()
}
