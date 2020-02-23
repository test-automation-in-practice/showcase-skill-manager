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
    val skills: Map<Skill, Knowledge>,
    val projects: List<ProjectAssignment>
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

data class Knowledge(
    val level: SkillLevel
)

data class SkillLevel @JsonCreator constructor(
    @JsonValue private val value: Int
) {
    override fun toString() = value.toString()
}

data class ProjectAssignment(
    val id: UUID,
    val project: Project,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

data class ProjectContribution @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}
