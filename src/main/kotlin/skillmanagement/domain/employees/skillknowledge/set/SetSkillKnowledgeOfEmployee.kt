package skillmanagement.domain.employees.skillknowledge.set

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.skills.get.GetSkillById
import java.util.UUID

@BusinessFunction
class SetSkillKnowledgeOfEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val getSkillById: GetSkillById,
    private val setEmployeeSkillInDataStore: SetEmployeeSkillInDataStore
) {

    // TODO: Security - Only invokable by Employee themselves or Employee-Admins
    operator fun invoke(
        employeeId: UUID,
        skillId: UUID,
        level: SkillLevel,
        secret: Boolean
    ): SetSkillKnowledgeOfEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val skill = getSkillById(skillId) ?: return SkillNotFound

        val skillKnowledge = SkillKnowledge(skill = skill, level = level, secret = secret)
        setEmployeeSkillInDataStore(employee, skillKnowledge)

        return SuccessfullyAssigned(skillKnowledge)
    }

}

sealed class SetSkillKnowledgeOfEmployeeResult {
    object EmployeeNotFound : SetSkillKnowledgeOfEmployeeResult()
    object SkillNotFound : SetSkillKnowledgeOfEmployeeResult()
    data class SuccessfullyAssigned(val skillKnowledge: SkillKnowledge) : SetSkillKnowledgeOfEmployeeResult()
}
