package skillmanagement.domain.employees.model

import java.time.LocalDate

internal data class EmployeeChangeData(
    val firstName: FirstName,
    val lastName: LastName,
    val title: JobTitle,
    val email: EmailAddress,
    val telephone: TelephoneNumber,

    val description: EmployeeDescription?,
    val academicDegrees: List<AcademicDegree>,
    val certifications: List<Certification>,
    val publications: List<Publication>,
    val languages: List<LanguageProficiency>,
    val jobHistory: List<Job>
)

internal data class ProjectAssignmentChangeData(
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

internal data class SkillKnowledgeChangeData(
    val level: SkillLevel,
    val secret: Boolean
)

internal fun Employee.toChangeData(): EmployeeChangeData =
    EmployeeChangeData(
        firstName = firstName,
        lastName = lastName,
        title = title,
        email = email,
        telephone = telephone,
        description = description,
        academicDegrees = academicDegrees,
        certifications = certifications,
        publications = publications,
        languages = languages,
        jobHistory = jobHistory
    )

internal fun Employee.merge(changes: EmployeeChangeData): Employee =
    copy(
        firstName = changes.firstName,
        lastName = changes.lastName,
        title = changes.title,
        email = changes.email,
        telephone = changes.telephone,
        description = changes.description,
        academicDegrees = changes.academicDegrees,
        certifications = changes.certifications,
        publications = changes.publications,
        languages = changes.languages,
        jobHistory = changes.jobHistory
    )

internal fun ProjectAssignment.toChangeData(): ProjectAssignmentChangeData =
    ProjectAssignmentChangeData(contribution = contribution, startDate = startDate, endDate = endDate)

internal fun ProjectAssignment.merge(changes: ProjectAssignmentChangeData): ProjectAssignment =
    copy(contribution = changes.contribution, startDate = changes.startDate, endDate = changes.endDate)

internal fun SkillKnowledge.toChangeData(): SkillKnowledgeChangeData =
    SkillKnowledgeChangeData(level = level, secret = secret)

internal fun SkillKnowledge.merge(changes: SkillKnowledgeChangeData): SkillKnowledge =
    copy(level = changes.level, secret = changes.secret)
