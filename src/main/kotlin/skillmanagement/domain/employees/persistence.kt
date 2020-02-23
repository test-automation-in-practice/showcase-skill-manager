package skillmanagement.domain.employees

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EmployeeRepository : MongoRepository<EmployeeDocument, UUID>

@Document("employees")
data class EmployeeDocument(
    @Id val id: UUID,
    val firstName: FirstName,
    val lastName: LastName,
    val skills: Map<UUID, SkillAssignment>,
    val projects: Map<UUID, ProjectAssignment>
)

fun Employee.toDocument() = EmployeeDocument(
    id = id,
    firstName = firstName,
    lastName = lastName,
    skills = skills.map { (skill, assignment) -> skill.id to assignment }.toMap(),
    projects = projects.map { (project, assignment) -> project.id to assignment }.toMap()
)
