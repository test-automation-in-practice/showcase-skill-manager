package skillmanagement.domain.employees.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.common.searchindices.Page
import skillmanagement.common.searchindices.toMetaData
import java.time.Instant
import java.time.LocalDate
import java.util.*

private const val RESOURCE_BASE = "api/employees"

@JsonInclude(NON_NULL)
@Relation(itemRelation = "employee", collectionRelation = "employees")
internal data class EmployeeResource(
    val id: UUID,

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
    val jobHistory: List<Job>,

    val skills: List<SkillKnowledgeResource>,
    val projects: List<ProjectAssignmentResource>,

    val lastUpdate: Instant
) : RepresentationModel<EmployeeResource>()

internal data class SkillKnowledgeResource(
    val label: String,
    val level: SkillLevel,
    val secret: Boolean
) : RepresentationModel<SkillKnowledgeResource>()

internal data class ProjectAssignmentResource(
    val label: String,
    val description: String,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
) : RepresentationModel<SkillKnowledgeResource>()

internal fun Employee.toResource() = EmployeeResource(
    id = id,
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
    jobHistory = jobHistory,
    skills = skills.map { it.toResource(id) },
    projects = projects.map { it.toResource(id) },
    lastUpdate = lastUpdate
).apply {
    add(linkToEmployee(id).withSelfRel())
    add(linkToEmployee(id).withRel("delete"))
}

internal fun SkillKnowledge.toResource(employeeId: UUID) = SkillKnowledgeResource(
    label = skill.label,
    level = level,
    secret = secret
).apply {
    add(linkToSkillKnowledge(employeeId, skill.id).withSelfRel())
    add(linkToEmployee(employeeId).withRel("employee"))
    add(linkToSkill(skill.id).withRel("skill"))
}

internal fun ProjectAssignment.toResource(employeeId: UUID) = ProjectAssignmentResource(
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

internal fun Page<Employee>.toAllResource(): PagedModel<EmployeeResource> =
    PagedModel.of(content.map(Employee::toResource), toMetaData())
        .apply {
            add(linkToEmployees(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToEmployees(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToEmployees(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToEmployees(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash(RESOURCE_BASE + queryPart)
}

internal fun Page<Employee>.toSearchResource(): PagedModel<EmployeeResource> =
    PagedModel.of(content.map(Employee::toResource), toMetaData())
        .apply {
            add(linkToEmployeesSearch(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToEmployeesSearch(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToEmployeesSearch(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToEmployeesSearch(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "/_search?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash(RESOURCE_BASE + queryPart)
}

internal fun linkToEmployee(id: UUID) =
    linkToCurrentMapping().slash("$RESOURCE_BASE/$id")

internal fun linkToSkillKnowledge(employeeId: UUID, skillId: UUID) =
    linkToCurrentMapping().slash("$RESOURCE_BASE/$employeeId/skills/$skillId")

internal fun linkToSkill(id: UUID) =
    linkToCurrentMapping().slash("api/employees/$id")

internal fun linkToProjectAssignment(employeeId: UUID, assignmentId: UUID) =
    linkToCurrentMapping().slash("$RESOURCE_BASE/$employeeId/projects/$assignmentId")

internal fun linkToProject(id: UUID) =
    linkToCurrentMapping().slash("api/projects/$id")
