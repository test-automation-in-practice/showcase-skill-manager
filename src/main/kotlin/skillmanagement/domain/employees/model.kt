package skillmanagement.domain.employees

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.domain.Validation.Companion.validate
import skillmanagement.domain.isGreaterThanOrEqualTo
import skillmanagement.domain.isLessThanOrEqualTo
import skillmanagement.domain.projects.Project
import skillmanagement.domain.skills.model.Skill
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

// TODO:
//  - Zertifizierungen
//  - Publikationen
//  - "Beratungsschwerpunkt und Schlüsselqualifikationen"
//  - Beruflicher Werdegang
//     - von bis
//     - bis ist optional
//     - Firma + Titel
//  - Ausbildung
//     - Abschluss
//     - Sprachkennisse

data class Employee(
    val id: UUID,
    val version: Int,
    val firstName: FirstName,
    val lastName: LastName,
    val title: Title,
    val email: EmailAddress,
    val telephone: TelephoneNumber,
    val skills: List<SkillKnowledge>,
    val projects: List<ProjectAssignment>,
    val lastUpdate: Instant
) {

    fun hasSkillKnowledgeBySkillId(skillId: UUID): Boolean =
        skills.any { it.skill.id == skillId }

    fun setSkillKnowledge(skillKnowledge: SkillKnowledge): Employee =
        copy(skills = skills.filter { it.skill.id != skillKnowledge.skill.id } + skillKnowledge)

    fun updateSkillKnowledgeOfSkill(skill: Skill): Employee =
        copy(skills = skills.map {
            when (it.skill.id) {
                skill.id -> it.copy(skill = skill)
                else -> it
            }
        })

    fun removeSkillKnowledgeBySkillId(skillId: UUID): Employee =
        copy(skills = skills.filter { it.skill.id != skillId })

    fun hasProjectAssignmentById(assignmentId: UUID): Boolean =
        projects.any { it.id == assignmentId }

    fun setProjectAssignment(projectAssignment: ProjectAssignment): Employee = this
        .removeProjectAssignmentById(projectAssignment.id)
        .addProjectAssignment(projectAssignment)

    fun addProjectAssignment(projectAssignment: ProjectAssignment): Employee =
        copy(projects = projects + projectAssignment)

    fun updateProjectAssignmentsOfProject(project: Project): Employee =
        copy(projects = projects.map {
            when (it.project.id) {
                project.id -> it.copy(project = project)
                else -> it
            }
        })

    fun removeProjectAssignmentById(assignmentId: UUID): Employee =
        copy(projects = projects.filter { it.id != assignmentId })

    fun removeProjectAssignmentsByProjectId(projectId: UUID): Employee =
        copy(projects = projects.filter { it.project.id != projectId })

}

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

data class Title @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class EmailAddress @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class TelephoneNumber @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

// TODO:
//  - Kategorie / Gruppe in der der Employee den Skill sieht

data class SkillKnowledge(
    val skill: Skill,
    val level: SkillLevel,
    val secret: Boolean
)

data class SkillLevel @JsonCreator constructor(
    @JsonValue private val value: Int
) {

    init {
        validate(value, "Skill Level") {
            isGreaterThanOrEqualTo(1)
            isLessThanOrEqualTo(10)
        }
    }

    fun toInt() = value
    override fun toString() = value.toString()

}

// TODO:
//  - Qualifikationen / Eingesetzte Skills

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
