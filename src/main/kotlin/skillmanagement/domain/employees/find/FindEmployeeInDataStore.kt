package skillmanagement.domain.employees.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.EmployeeRowMapper
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectAssignmentRowMapper
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillKnowledgeRowMapper
import java.util.UUID

@TechnicalFunction
class FindEmployeeInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeesQuery = "SELECT * FROM employees"
    private val employeesSkillsQuery = """
        SELECT es.*, s.label
        FROM employee_skills es
        LEFT OUTER JOIN skills s
          ON es.skill_id = s.id
        WHERE es.employee_id IN (:ids)
        """.trimIndent()
    private val employeesProjectsQuery = """
        SELECT epa.*, p.label, p.description
        FROM employee_project_assignments epa
        LEFT OUTER JOIN projects p
          ON epa.project_id = p.id
        WHERE epa.employee_id IN (:ids)
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(): List<Employee> {
        val employees = getEmployeesBaseData()
        if (employees.isEmpty()) {
            return emptyList()
        }

        val employeeIds = employees.map { it.id }
        val employeeSkillsMap = getEmployeeSkills(employeeIds)
        val employeeProjectsMap = getEmployeeProjects(employeeIds)

        return employees.map { employee ->
            val skills = employeeSkillsMap[employee.id] ?: emptyList()
            val projects = employeeProjectsMap[employee.id] ?: emptyList()

            employee.copy(skills = skills, projects = projects)
        }
    }

    private fun getEmployeesBaseData(): List<Employee> =
        jdbcTemplate.query(employeesQuery, EmployeeRowMapper)

    private fun getEmployeeSkills(ids: Collection<UUID>): Map<UUID, List<SkillKnowledge>> {
        val map = mutableMapOf<UUID, MutableList<SkillKnowledge>>()
        jdbcTemplate.query(employeesSkillsQuery, mapOf("ids" to ids.map { "$it" })) { rs ->
            val employeeId = rs.getString("employee_id").let { UUID.fromString(it) }
            val skillKnowledge = SkillKnowledgeRowMapper.mapRow(rs, -1)

            map.computeIfAbsent(employeeId) { mutableListOf() }.add(skillKnowledge)
        }
        return map
    }

    private fun getEmployeeProjects(ids: Collection<UUID>): Map<UUID, List<ProjectAssignment>> {
        val map = mutableMapOf<UUID, MutableList<ProjectAssignment>>()
        jdbcTemplate.query(employeesProjectsQuery, mapOf("ids" to ids.map { "$it" })) { rs ->
            val employeeId = rs.getString("employee_id").let { UUID.fromString(it) }
            val projectAssignment = ProjectAssignmentRowMapper.mapRow(rs, -1)

            map.computeIfAbsent(employeeId) { mutableListOf() }.add(projectAssignment)
        }
        return map
    }

}
