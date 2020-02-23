package skillmanagement.domain.employees.skills.assign

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.employees.Knowledge
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.get.GetEmployeeById
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.get.GetSkillById
import java.util.*

@BusinessFunction
class AssignSkillToEmployee(
    private val getEmployeeById: GetEmployeeById,
    private val getSkillById: GetSkillById,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    // TODO: Security - Only invokable by Employee-Admins
    operator fun invoke(employeeId: UUID, skillId: UUID, level: SkillLevel): AssignSkillToEmployeeResult {
        val employee = getEmployeeById(employeeId) ?: return EmployeeNotFound
        val skill = getSkillById(skillId) ?: return SkillNotFound
        val knowledge = Knowledge(level = level)

        val updatedSkills = employee.skills
            .filterKeys { it.id != skillId }
            .toMutableMap()
            .apply { put(skill, knowledge) }

        // TODO: use direct update statement instead of updating the whole document
        // https://stackoverflow.com/questions/38261838/add-object-to-an-array-in-java-mongodb
        updateEmployeeInDataStore(employee.copy(skills = updatedSkills))

        return SuccessfullyAssigned(skill, knowledge)
    }

}

sealed class AssignSkillToEmployeeResult {
    object EmployeeNotFound : AssignSkillToEmployeeResult()
    object SkillNotFound : AssignSkillToEmployeeResult()
    data class SuccessfullyAssigned(val skill: Skill, val knowledge: Knowledge) : AssignSkillToEmployeeResult()
}
