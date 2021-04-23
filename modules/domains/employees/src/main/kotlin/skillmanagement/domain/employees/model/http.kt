package skillmanagement.domain.employees.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.BasicLinkBuilder
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import skillmanagement.common.http.toMetaData
import skillmanagement.common.model.Page
import java.time.LocalDate

@JsonInclude(NON_NULL)
@Relation(itemRelation = "employee", collectionRelation = "employees")
internal data class EmployeeRepresentation(
    val id: EmployeeId,
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
    val skills: List<SkillKnowledgeRepresentation>,
    val projects: List<ProjectAssignmentRepresentation>
) : RepresentationModel<EmployeeRepresentation>()

internal data class SkillKnowledgeRepresentation(
    val label: String,
    val level: SkillLevel,
    val secret: Boolean
) : RepresentationModel<SkillKnowledgeRepresentation>()

internal data class ProjectAssignmentRepresentation(
    val label: String,
    val description: String,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
) : RepresentationModel<SkillKnowledgeRepresentation>()

internal fun EmployeeEntity.toRepresentation() =
    EmployeeRepresentation(
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
        skills = skills.map(SkillKnowledge::toRepresentation),
        projects = projects.map(ProjectAssignment::toRepresentation)
    )

internal fun SkillKnowledge.toRepresentation() =
    SkillKnowledgeRepresentation(
        label = skill.label,
        level = level,
        secret = secret
    )

internal fun ProjectAssignment.toRepresentation() =
    ProjectAssignmentRepresentation(
        label = project.label,
        description = project.description,
        contribution = contribution,
        startDate = startDate,
        endDate = endDate
    )

internal fun EmployeeEntity.toResource() =
    EmployeeRepresentation(
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
        skills = skills.map { knowledge -> knowledge.toResource(id) },
        projects = projects.map { assignment -> assignment.toResource(id) }
    ).apply {
        add(linkToEmployee(id).withSelfRel())
        add(linkToEmployee(id).withRel("delete"))
    }

internal fun SkillKnowledge.toResource(employeeId: EmployeeId) = toRepresentation()
    .apply {
        add(linkToSkillKnowledge(employeeId, skill.id).withSelfRel())
        add(linkToEmployee(employeeId).withRel("employee"))
        add(linkToSkill(skill.id).withRel("skill"))
    }

internal fun ProjectAssignment.toResource(employeeId: EmployeeId) = toRepresentation()
    .apply {
        add(linkToProjectAssignment(employeeId, id).withSelfRel())
        add(linkToEmployee(employeeId).withRel("employee"))
        add(linkToProject(project.id).withRel("project"))
    }

internal fun Page<EmployeeEntity>.toRepresentations() = withOtherContent(content.map(EmployeeEntity::toRepresentation))

internal fun Page<EmployeeEntity>.toResource(): PagedModel<EmployeeRepresentation> =
    PagedModel.of(content.map(EmployeeEntity::toResource), toMetaData())
        .apply {
            add(linkToEmployees(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToEmployees(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToEmployees(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToEmployees(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/employees" + queryPart)
}

internal fun Page<EmployeeEntity>.toSearchResource(): PagedModel<EmployeeRepresentation> =
    PagedModel.of(content.map(EmployeeEntity::toResource), toMetaData())
        .apply {
            add(linkToEmployeesSearch(pageIndex, pageSize).withSelfRel())
            if (hasPrevious()) add(linkToEmployeesSearch(pageIndex - 1, pageSize).withRel("previousPage"))
            if (hasNext()) add(linkToEmployeesSearch(pageIndex + 1, pageSize).withRel("nextPage"))
        }

internal fun linkToEmployeesSearch(pageIndex: Int, pageSize: Int): BasicLinkBuilder {
    val queryPart = "/_search?page=$pageIndex&size=$pageSize"
    return linkToCurrentMapping().slash("api/employees" + queryPart)
}

internal fun linkToEmployee(id: EmployeeId) =
    linkToCurrentMapping().slash("api/employees/$id")

internal fun linkToSkillKnowledge(employeeId: EmployeeId, skillId: SkillId) =
    linkToCurrentMapping().slash("api/employees/$employeeId/skills/$skillId")

internal fun linkToSkill(id: SkillId) =
    linkToCurrentMapping().slash("api/employees/$id")

internal fun linkToProjectAssignment(employeeId: EmployeeId, assignmentId: ProjectAssignmentId) =
    linkToCurrentMapping().slash("api/employees/$employeeId/projects/$assignmentId")

internal fun linkToProject(id: ProjectId) =
    linkToCurrentMapping().slash("api/projects/$id")
