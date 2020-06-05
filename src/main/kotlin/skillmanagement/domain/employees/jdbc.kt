package skillmanagement.domain.employees

import org.springframework.jdbc.core.RowMapper
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import java.sql.ResultSet
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

object EmployeeRowMapper : RowMapper<Employee> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = Employee(
        id = rs.id,
        firstName = rs.firstName,
        lastName = rs.lastName,
        title = rs.title,
        email = rs.email,
        telephone = rs.telephone,
        lastUpdate = rs.lastUpdate
    )

    private val ResultSet.id: UUID
        get() = UUID.fromString(getString("id"))
    private val ResultSet.firstName: FirstName
        get() = FirstName(getString("first_name"))
    private val ResultSet.lastName: LastName
        get() = LastName(getString("last_name"))
    private val ResultSet.title: Title
        get() = Title(getString("title"))
    private val ResultSet.email: EmailAddress
        get() = EmailAddress(getString("email"))
    private val ResultSet.telephone: TelephoneNumber
        get() = TelephoneNumber(getString("telephone"))
    private val ResultSet.lastUpdate: Instant
        get() = Instant.parse(getString("last_update_utc"))

}

object SkillKnowledgeRowMapper : RowMapper<SkillKnowledge> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        SkillKnowledge(
            skill = Skill(
                id = rs.skillId,
                label = rs.skillLabel
            ),
            level = rs.level,
            secret = rs.secret
        )

    private val ResultSet.skillId: UUID
        get() = UUID.fromString(getString("skill_id"))
    private val ResultSet.skillLabel: SkillLabel
        get() = SkillLabel(getString("label"))
    private val ResultSet.level: SkillLevel
        get() = SkillLevel(getInt("level"))
    private val ResultSet.secret: Boolean
        get() = getBoolean("secret")

}

object ProjectAssignmentRowMapper : RowMapper<ProjectAssignment> {

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
        get() = UUID.fromString(getString("id"))
    private val ResultSet.projectId: UUID
        get() = UUID.fromString(getString("project_id"))
    private val ResultSet.projectLabel: ProjectLabel
        get() = ProjectLabel(getString("label"))
    private val ResultSet.projectDescription: ProjectDescription
        get() = ProjectDescription(getString("description"))
    private val ResultSet.contribution: ProjectContribution
        get() = ProjectContribution(getString("contribution"))
    private val ResultSet.startDate: LocalDate
        get() = LocalDate.parse(getString("start_date"))
    private val ResultSet.endDate: LocalDate?
        get() = LocalDate.parse(getString("end_date"))

}
