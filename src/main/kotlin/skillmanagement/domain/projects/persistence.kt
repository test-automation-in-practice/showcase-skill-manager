package skillmanagement.domain.projects

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ProjectRepository : MongoRepository<ProjectDocument, UUID>

@Document("projects")
data class ProjectDocument(
    @Id val id: UUID,
    val label: ProjectLabel,
    val description: ProjectDescription
)

fun ProjectDocument.toProject() = Project(
    id = id,
    label = label,
    description = description
)

fun Project.toDocument() = ProjectDocument(
    id = id,
    label = label,
    description = description
)
