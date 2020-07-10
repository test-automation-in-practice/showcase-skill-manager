package skillmanagement.domain.employees

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.projects.linkToProject
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.linkToSkill
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@JsonInclude(NON_NULL)
@Relation(itemRelation = "employee", collectionRelation = "employees")
data class EmployeeResource(
    val id: UUID,
    val firstName: FirstName,
    val lastName: LastName,
    val title: Title,
    val email: EmailAddress,
    val telephone: TelephoneNumber,
    val skills: List<SkillAssignmentResource>?,
    val projects: List<ProjectAssignmentResource>?,
    val lastUpdate: Instant
) : RepresentationModel<EmployeeResource>()

data class SkillAssignmentResource(
    val label: SkillLabel,
    val level: SkillLevel,
    val secret: Boolean
) : RepresentationModel<SkillAssignmentResource>()

data class ProjectAssignmentResource(
    val label: ProjectLabel,
    val description: ProjectDescription,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
) : RepresentationModel<SkillAssignmentResource>()

fun Employee.toResource() = EmployeeResource(
    id = id,
    firstName = firstName,
    lastName = lastName,
    title = title,
    email = email,
    telephone = telephone,
    skills = skills.map { it.toResources(id) },
    projects = projects.map { it.toResources(id) },
    lastUpdate = lastUpdate
).apply {
    add(linkToEmployee(id).withSelfRel())
    add(linkToEmployee(id).withRel("delete"))
}

fun SkillKnowledge.toResources(employeeId: UUID) = SkillAssignmentResource(
    label = skill.label,
    level = level,
    secret = secret
).apply {
    add(linkToSkillKnowledge(employeeId, skill.id).withSelfRel())
    add(linkToEmployee(employeeId).withRel("employee"))
    add(linkToSkill(skill.id).withRel("skill"))
}

fun ProjectAssignment.toResources(employeeId: UUID) = ProjectAssignmentResource(
    label = project.label,
    description = project.description,
    contribution = contribution,
    startDate = startDate,
    endDate = endDate
).apply {
    add(linkToProjectAssignment(employeeId, id).withSelfRel())
    add(linkToEmployee(employeeId).withRel("employee"))
    add(linkToProject(project.id).withRel("project"))
}

fun linkToEmployee(id: UUID) =
    linkToCurrentMapping().slash(pathToEmployee(id))

fun linkToSkillKnowledge(employeeId: UUID, skillId: UUID) =
    linkToCurrentMapping().slash("${pathToEmployee(employeeId)}/skills/$skillId")

fun linkToProjectAssignment(employeeId: UUID, assignmentId: UUID) =
    linkToCurrentMapping().slash("${pathToEmployee(employeeId)}/projects/$assignmentId")

private fun pathToEmployee(id: UUID) = "api/employees/$id"
