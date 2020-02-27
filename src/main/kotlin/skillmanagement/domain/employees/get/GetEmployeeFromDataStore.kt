package skillmanagement.domain.employees.get

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import java.sql.ResultSet
import java.time.LocalDate
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

private object EmployeeRowMapper : RowMapper<Employee> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = Employee(
        id = rs.id,
        firstName = rs.firstName,
        lastName = rs.lastName,
        skills = emptyList(),
        projects = emptyList()
    )

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.firstName: FirstName
        get() = getString("first_name").let(::FirstName)
    private val ResultSet.lastName: LastName
        get() = getString("last_name").let(::LastName)
}

private object SkillKnowledgeRowMapper : RowMapper<SkillKnowledge> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        SkillKnowledge(
            skill = Skill(
                id = rs.skillId,
                label = rs.skillLabel
            ),
            level = rs.level
        )

    private val ResultSet.skillId: UUID
        get() = getString("skill_id").let { UUID.fromString(it) }
    private val ResultSet.skillLabel: SkillLabel
        get() = getString("label").let(::SkillLabel)
    private val ResultSet.level: SkillLevel
        get() = getInt("level").let(::SkillLevel)
}

private object ProjectAssignmentRowMapper : RowMapper<ProjectAssignment> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        ProjectAssignment(
            id = rs.id,
            project = Project(
                id = rs.projectId,
                label = rs.projectLabel,
                description = rs.projectDescription
            ),
            contribution = rs.contribution,
            startDate = rs.startDate,
            endDate = rs.endDate
        )

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.projectId: UUID
        get() = getString("project_id").let { UUID.fromString(it) }
    private val ResultSet.projectLabel: ProjectLabel
        get() = getString("label").let(::ProjectLabel)
    private val ResultSet.projectDescription: ProjectDescription
        get() = getString("description").let(::ProjectDescription)
    private val ResultSet.contribution: ProjectContribution
        get() = getString("contribution").let(::ProjectContribution)
    private val ResultSet.startDate: LocalDate
        get() = getString("start_date").let { LocalDate.parse(it) }
    private val ResultSet.endDate: LocalDate?
        get() = getString("end_date")?.let { LocalDate.parse(it) }
}
