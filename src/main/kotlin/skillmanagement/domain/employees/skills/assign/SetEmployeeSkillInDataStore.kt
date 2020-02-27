package skillmanagement.domain.employees.skills.assign

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.SkillKnowledge

@TechnicalFunction
class SetEmployeeSkillInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val statement = "DELETE FROM employee_skills WHERE employee_id = :employee_id AND skill_id = :skill_id"

    operator fun invoke(employee: Employee, skillKnowledge: SkillKnowledge) {
        deleteExistingEntry(employee, skillKnowledge)
        createNewEntry(employee, skillKnowledge)
    }

    private fun deleteExistingEntry(employee: Employee, skillKnowledge: SkillKnowledge) {
        val parameters = mapOf(
            "employee_id" to employee.id.toString(),
            "skill_id" to skillKnowledge.skill.id.toString()
        )
        jdbcTemplate.update(statement, parameters)
    }

    private fun createNewEntry(employee: Employee, skillKnowledge: SkillKnowledge) {
        jdbcTemplate.insert(
            tableName = "employee_skills",
            columnValueMapping = listOf(
                "employee_id" to employee.id.toString(),
                "skill_id" to skillKnowledge.skill.id.toString(),
                "level" to skillKnowledge.level.toInt()
            )
        )
    }

}
