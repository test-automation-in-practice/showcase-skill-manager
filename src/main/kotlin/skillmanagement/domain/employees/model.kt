package skillmanagement.domain.employees

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.domain.projects.Project
import skillmanagement.domain.skills.Skill
import java.time.LocalDate
import java.util.*

data class Employee(
    val id: UUID,
    val firstName: FirstName,
    val lastName: LastName,
    val skills: Map<Skill, SkillAssignment>,
    val projects: Map<Project, ProjectAssignment>
)

data class FirstName @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class LastName @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class SkillAssignment(
    val level: SkillLevel
)

data class SkillLevel @JsonCreator constructor(
    @JsonValue private val value: Int
) {
    override fun toString() = value.toString()
}

// TODO: what if an employee was assigned to the same project multiple times over the years?
data class ProjectAssignment(
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

data class ProjectContribution @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}
