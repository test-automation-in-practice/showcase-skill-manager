package skillmanagement.domain.employees.get

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
class GetEmployeeFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeeQuery = "SELECT * FROM employees WHERE id = :id"
    private val employeeSkillsQuery = """
        SELECT es.*, s.label
        FROM employee_skills es
        LEFT OUTER JOIN skills s
          ON es.skill_id = s.id
        WHERE es.employee_id = :id
        """.trimIndent()
    private val employeeProjectsQuery = """
        SELECT epa.*, p.label, p.description
        FROM employee_project_assignments epa
        LEFT OUTER JOIN projects p
          ON epa.project_id = p.id
        WHERE epa.employee_id = :id
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(id: UUID): Employee? = getEmployeeBaseData(id)
        ?.copy(skills = getEmployeeSkills(id), projects = getEmployeeProjects(id))

    private fun getEmployeeBaseData(id: UUID): Employee? =
        jdbcTemplate.query(employeeQuery, mapOf("id" to "$id"), EmployeeRowMapper)
            .firstOrNull()

    private fun getEmployeeSkills(id: UUID): List<SkillKnowledge> =
        jdbcTemplate.query(employeeSkillsQuery, mapOf("id" to "$id"), SkillKnowledgeRowMapper)

    private fun getEmployeeProjects(id: UUID): List<ProjectAssignment> =
        jdbcTemplate.query(employeeProjectsQuery, mapOf("id" to "$id"), ProjectAssignmentRowMapper)

}
