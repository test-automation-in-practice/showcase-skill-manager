package skillmanagement.domain.employees.usecases.skillknowledge.assign

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.SkillKnowledge
import skillmanagement.domain.employees.model.SkillLevel
import skillmanagement.domain.employees.usecases.get.GetEmployeeById
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.assign.SetSkillKnowledgeOfEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.usecases.update.RetryOnConcurrentEmployeeUpdate
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeInDataStore
import skillmanagement.domain.skills.usecases.get.GetSkillById
import java.util.UUID

@BusinessFunction
class AssignSkillKnowledgeOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val getSkillById: GetSkillById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    @RetryOnConcurrentEmployeeUpdate
    operator fun invoke(
        employeeId: UUID,
        skillId: UUID,
        level: SkillLevel,
        secret: Boolean
    ): SetSkillKnowledgeOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val skill = getSkillById(skillId) ?: return SkillNotFound

        val skillKnowledge = SkillKnowledge(
            skill = skill,
            level = level,
            secret = secret
        )

        val updatedEmployee = employee.setSkillKnowledge(skillKnowledge)
        updateEmployeeInDataStore(updatedEmployee)
        return SuccessfullyAssigned(skillKnowledge)
    }

}

sealed class SetSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : SetSkillKnowledgeOfEmployeeResult()
    object SkillNotFound : SetSkillKnowledgeOfEmployeeResult()
    data class SuccessfullyAssigned(val skillKnowledge: SkillKnowledge) : SetSkillKnowledgeOfEmployeeResult()
}
