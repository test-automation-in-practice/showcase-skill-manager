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
        skills = emptyList(),
        projects = emptyList(),
        lastUpdate = rs.lastUpdate
    )

    private val ResultSet.id: UUID
        get() = getString("id").let { UUID.fromString(it) }
    private val ResultSet.firstName: FirstName
        get() = getString("first_name").let(::FirstName)
    private val ResultSet.lastName: LastName
        get() = getString("last_name").let(::LastName)
    private val ResultSet.title: Title
        get() = getString("title").let(::Title)
    private val ResultSet.email: EmailAddress
        get() = getString("email").let(::EmailAddress)
    private val ResultSet.telephone: TelephoneNumber
        get() = getString("telephone").let(::TelephoneNumber)
    private val ResultSet.lastUpdate: Instant
        get() = getString("last_update_utc").let { Instant.parse(it) }
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
        get() = getString("skill_id").let { UUID.fromString(it) }
    private val ResultSet.skillLabel: SkillLabel
        get() = getString("label").let(::SkillLabel)
    private val ResultSet.level: SkillLevel
        get() = getInt("level").let(::SkillLevel)
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
