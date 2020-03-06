package skillmanagement.domain.employees.skillknowledge.delete

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.delete
import skillmanagement.common.update
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.SkillKnowledge
import java.time.Clock

@TechnicalFunction
class DeleteSkillKnowledgeFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val clock: Clock
) {

    @Transactional
    operator fun invoke(employee: Employee, knowledge: SkillKnowledge) {
        deleteExistingEntry(employee, knowledge)
        updateEmployee(employee)
    }

    private fun deleteExistingEntry(employee: Employee, knowledge: SkillKnowledge) {
        jdbcTemplate.delete(
            table = "employee_skills",
            whereConditions = listOf(
                "employee_id" to employee.id.toString(),
                "skill_id" to knowledge.skill.id.toString()
            )
        )
    }

    private fun updateEmployee(employee: Employee) {
        jdbcTemplate.update(
            table = "employees",
            columnValues = listOf("last_update_utc" to clock.instant().toString()),
            whereConditions = listOf("id" to employee.id.toString())
        )
    }

}
