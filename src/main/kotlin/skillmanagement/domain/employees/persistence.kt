package skillmanagement.domain.employees

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import skillmanagement.domain.projects.Project
import skillmanagement.domain.skills.Skill
import java.time.LocalDate
import java.util.*

interface EmployeeRepository : MongoRepository<EmployeeDocument, UUID>

@Document("employees")
data class EmployeeDocument(
    @Id val id: UUID,
    val firstName: FirstName,
    val lastName: LastName,
    val skills: Map<UUID, Knowledge>,
    val projects: List<ProjectAssignmentPart>
)

data class ProjectAssignmentPart(
    val id: UUID,
    val projectId: UUID,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

fun EmployeeDocument.toEmployee(
    skillResolver: (Collection<UUID>) -> Map<UUID, Skill>,
    projectResolver: (Collection<UUID>) -> Map<UUID, Project>
): Employee {
    val resolvedSkills = skillResolver(skills.keys)
    val resolvedProjects = projectResolver(projects.map(ProjectAssignmentPart::projectId))
    return Employee(
        id = id,
        firstName = firstName,
        lastName = lastName,
        skills = skills.filterKeys { resolvedSkills.containsKey(it) }
            .map { (skillId, knowledge) ->
                resolvedSkills.getValue(skillId) to knowledge
            }
            .toMap(),
        projects = projects.filter { resolvedProjects.containsKey(it.projectId) }
            .map {
                ProjectAssignment(
                    id = it.id,
                    project = resolvedProjects.getValue(it.projectId),
                    contribution = it.contribution,
                    startDate = it.startDate,
                    endDate = it.endDate
                )
            }
    )
}

fun Employee.toDocument() = EmployeeDocument(
    id = id,
    firstName = firstName,
    lastName = lastName,
    skills = skills.map { (skill, knowledge) -> skill.id to knowledge }.toMap(),
    projects = projects.map {
        ProjectAssignmentPart(
            id = it.id,
            projectId = it.project.id,
            contribution = it.contribution,
            startDate = it.startDate,
            endDate = it.endDate
        )
    }
)
