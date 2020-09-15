package skillmanagement.domain.employees.model

import java.time.LocalDate

data class EmployeeChangeData(
    val firstName: FirstName,
    val lastName: LastName,
    val title: Title,
    val email: EmailAddress,
    val telephone: TelephoneNumber
)

data class ProjectAssignmentChangeData(
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

data class SkillKnowledgeChangeData(
    val level: SkillLevel,
    val secret: Boolean
)

fun Employee.toChangeData(): EmployeeChangeData =
    EmployeeChangeData(
        firstName = firstName,
        lastName = lastName,
        title = title,
        email = email,
        telephone = telephone
    )

fun Employee.merge(changes: EmployeeChangeData): Employee =
    copy(
        firstName = changes.firstName,
        lastName = changes.lastName,
        title = changes.title,
        email = changes.email,
        telephone = changes.telephone
    )

fun ProjectAssignment.toChangeData(): ProjectAssignmentChangeData =
    ProjectAssignmentChangeData(contribution = contribution, startDate = startDate, endDate = endDate)

fun ProjectAssignment.merge(changes: ProjectAssignmentChangeData): ProjectAssignment =
    copy(contribution = changes.contribution, startDate = changes.startDate, endDate = changes.endDate)

fun SkillKnowledge.toChangeData(): SkillKnowledgeChangeData =
    SkillKnowledgeChangeData(level = level, secret = secret)

fun SkillKnowledge.merge(changes: SkillKnowledgeChangeData): SkillKnowledge =
    copy(level = changes.level, secret = changes.secret)
