package skillmanagement.domain.employees

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillLabel
import java.time.LocalDate
import java.util.*

data class EmployeeResource(
    val firstName: FirstName,
    val lastName: LastName,
    val skills: List<EmployeeSkillResource>,
    val projects: List<EmployeeProjectResource>
) : RepresentationModel<EmployeeResource>()

data class EmployeeSkillResource(
    val label: SkillLabel,
    val level: SkillLevel
) : RepresentationModel<EmployeeSkillResource>()

data class EmployeeProjectResource(
    val label: ProjectLabel,
    val description: ProjectDescription,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
) : RepresentationModel<EmployeeSkillResource>()

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

private fun Map<Skill, SkillAssignment>.toSkillResources(employeeId: UUID) = this
    .map { (skill, assignment) ->
        EmployeeSkillResource(
            label = skill.label,
            level = assignment.level
        ).apply {
            add(linkToCurrentMapping().slash("api/employees/${employeeId}/skills/${skill.id}").withSelfRel())
        }
    }
    .toList()

private fun Map<Project, ProjectAssignment>.toProjectResources(employeeId: UUID) = this
    .map { (project, assignment) ->
        EmployeeProjectResource(
            label = project.label,
            description = project.description,
            contribution = assignment.contribution,
            startDate = assignment.startDate,
            endDate = assignment.endDate
        ).apply {
            add(linkToCurrentMapping().slash("api/employees/${employeeId}/projects/${project.id}").withSelfRel())
        }
    }
    .toList()
