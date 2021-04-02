package skillmanagement.domain.employees.model

import skillmanagement.common.model.IdType
import skillmanagement.common.model.IntType
import skillmanagement.common.model.Name
import skillmanagement.common.model.StringType
import skillmanagement.common.model.Text
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

data class Employee(
    val id: EmployeeId,
    val version: Int,

    val firstName: FirstName,
    val lastName: LastName,
    val title: JobTitle,
    val email: EmailAddress,
    val telephone: TelephoneNumber,

    val description: EmployeeDescription? = null,
    val academicDegrees: List<AcademicDegree> = emptyList(),
    val certifications: List<Certification> = emptyList(),
    val publications: List<Publication> = emptyList(),
    val languages: List<LanguageProficiency> = emptyList(),
    val jobHistory: List<Job> = emptyList(),

    val skills: List<SkillKnowledge> = emptyList(),
    val projects: List<ProjectAssignment> = emptyList(),

    val lastUpdate: Instant
) {

    fun compositeName() = "$firstName $lastName"

    fun addOrUpdateProjectAssignment(assignment: ProjectAssignment): Employee =
        removeProjectAssignment { it.id == assignment.id }
            .copy(projects = projects + assignment)

    fun addOrUpdateSkillKnowledge(knowledge: SkillKnowledge): Employee =
        removeSkillKnowledge { it.skill.id == knowledge.skill.id }
            .copy(skills = skills + knowledge)

    fun removeProjectAssignment(shouldRemove: (ProjectAssignment) -> Boolean): Employee =
        copy(projects = projects.filter { !shouldRemove(it) })

    fun removeSkillKnowledge(shouldRemove: (SkillKnowledge) -> Boolean): Employee =
        copy(skills = skills.filter { !shouldRemove(it) })

    fun updateProjectAssignment(projectId: ProjectId, block: (ProjectAssignment) -> ProjectAssignment): Employee =
        copy(projects = projects.map { if (it.project.id == projectId) block(it) else it })

    fun updateSkillKnowledge(skillId: SkillId, block: (SkillKnowledge) -> SkillKnowledge): Employee =
        copy(skills = skills.map { if (it.skill.id == skillId) block(it) else it })

}

class EmployeeId(value: UUID) : IdType(value)
class FirstName(value: String) : Name(value)
class LastName(value: String) : Name(value)
class EmployeeDescription(value: String) : Text(value)
class JobTitle(value: String) : StringType(value)
class EmailAddress(value: String) : StringType(value)
class TelephoneNumber(value: String) : StringType(value)
class Certification(value: String) : StringType(value)
class Publication(value: String) : StringType(value)

data class LanguageProficiency(
    val language: Language,
    val qualifier: LanguageQualifier
)

class Language(value: String) : StringType(value)
class LanguageQualifier(value: String) : StringType(value)

data class AcademicDegree(
    val subject: AcademicSubject,
    val title: AcademicTitle,
    val institution: AcademicInstitution
)

class AcademicSubject(value: String) : StringType(value)
class AcademicTitle(value: String) : StringType(value)
class AcademicInstitution(value: String) : StringType(value)

data class Job(
    val employer: Employer,
    val title: JobTitle,
    val start: YearMonth,
    val end: YearMonth? = null
)

class Employer(value: String) : StringType(value)

data class SkillKnowledge(
    val skill: SkillData,
    val level: SkillLevel,
    val secret: Boolean
)

data class SkillData(
    val id: SkillId,
    val label: String
)

class SkillId(value: UUID) : IdType(value)
class SkillLevel(value: Int) : IntType(value, min = 1, max = 10)

data class ProjectAssignment(
    val id: ProjectAssignmentId,
    val project: ProjectData,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

data class ProjectData(
    val id: ProjectId,
    val label: String,
    val description: String
)

class ProjectAssignmentId(value: UUID) : IdType(value)
class ProjectId(value: UUID) : IdType(value)
class ProjectContribution(value: String) : Text(value)

fun employeeId(value: String) = EmployeeId(UUID.fromString(value))
fun projectAssignmentId(value: String) = ProjectAssignmentId(UUID.fromString(value))

fun projectId(value: String) = ProjectId(UUID.fromString(value))
fun skillId(value: String) = SkillId(UUID.fromString(value))
