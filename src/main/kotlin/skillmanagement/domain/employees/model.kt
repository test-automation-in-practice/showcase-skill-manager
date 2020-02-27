package skillmanagement.domain.employees

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.domain.projects.Project
import skillmanagement.domain.skills.Skill
import java.time.LocalDate
import java.util.UUID

data class Employee(
    val id: UUID,
    val firstName: FirstName,
    val lastName: LastName,
    val skills: List<SkillKnowledge>,
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

data class SkillKnowledge(
    val skill: Skill,
    val level: SkillLevel
)

data class SkillLevel @JsonCreator constructor(
    @JsonValue private val value: Int
) {
    fun toInt() = value
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
