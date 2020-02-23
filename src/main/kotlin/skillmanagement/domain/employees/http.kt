package skillmanagement.domain.employees

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import java.time.LocalDate
import java.util.*

@Relation(itemRelation = "employee", collectionRelation = "employees")
data class EmployeeResource(
    val firstName: FirstName,
    val lastName: LastName,
    val skills: List<SkillAssignmentResource>,
    val projects: List<ProjectAssignmentResource>
) : RepresentationModel<EmployeeResource>()

data class SkillAssignmentResource(
    val label: SkillLabel,
    val level: SkillLevel
) : RepresentationModel<SkillAssignmentResource>()

data class ProjectAssignmentResource(
    val label: ProjectLabel,
    val description: ProjectDescription,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
) : RepresentationModel<SkillAssignmentResource>()

fun Employee.toResource(): EmployeeResource {
    val resource = EmployeeResource(
        firstName = firstName,
        lastName = lastName,
        skills = skills.toSkillResources(id),
        projects = projects.toProjectResources(id)
    )
    resource.add(linkToCurrentMapping().slash("api/employees/${id}").withSelfRel())
    return resource
}

fun Map<Skill, Knowledge>.toSkillResources(employeeId: UUID) = this
    .toList()
    .map { it.toResources(employeeId) }

fun Pair<Skill, Knowledge>.toResources(employeeId: UUID) =
    SkillAssignmentResource(
        label = first.label,
        level = second.level
    ).apply {
        add(linkToCurrentMapping().slash("api/employees/${employeeId}/skills/${first.id}").withSelfRel())
        add(linkToCurrentMapping().slash("api/employees/${employeeId}").withRel("employee"))
    }

fun List<ProjectAssignment>.toProjectResources(employeeId: UUID) =
    map { it.toResources(employeeId) }

fun ProjectAssignment.toResources(employeeId: UUID) =
    ProjectAssignmentResource(
        label = project.label,
        description = project.description,
        contribution = contribution,
        startDate = startDate,
        endDate = endDate
    ).apply {
        add(linkToCurrentMapping().slash("api/employees/${employeeId}/projects/${id}").withSelfRel())
        add(linkToCurrentMapping().slash("api/employees/${employeeId}").withRel("employee"))
    }
