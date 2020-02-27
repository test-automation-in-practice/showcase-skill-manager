package skillmanagement.domain.employees.skills.assign

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.skills.get.GetSkillById
import java.util.UUID

@BusinessFunction
class AssignSkillToEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val getSkillById: GetSkillById,
    private val setEmployeeSkillInDataStore: SetEmployeeSkillInDataStore
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(employeeId: UUID, skillId: UUID, level: SkillLevel): AssignSkillToEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val skill = getSkillById(skillId) ?: return SkillNotFound

        val skillKnowledge = SkillKnowledge(skill = skill, level = level)
        setEmployeeSkillInDataStore(employee, skillKnowledge)

        return SuccessfullyAssigned(skillKnowledge)
    }

}

sealed class AssignSkillToEmployeeResult {
    object EmployeeNotFound : AssignSkillToEmployeeResult()
    object SkillNotFound : AssignSkillToEmployeeResult()
    data class SuccessfullyAssigned(val skillKnowledge: SkillKnowledge) : AssignSkillToEmployeeResult()
}
