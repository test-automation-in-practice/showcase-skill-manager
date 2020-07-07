package skillmanagement.domain.projects.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.PublishEvent
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectAddedEvent
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel

@BusinessFunction
class AddProject(
    private val idGenerator: IdGenerator,
    private val insertProjectIntoDataStore: InsertProjectIntoDataStore,
    private val publishEvent: PublishEvent
) {

    // TODO: Security - Only invokable by Project-Admins
    operator fun invoke(label: ProjectLabel, description: ProjectDescription): Project {
        val project = Project(
            id = idGenerator.generateId(),
            label = label,
            description = description
        )
        insertProjectIntoDataStore(project)
        publishEvent(ProjectAddedEvent(project))
        return project
    }

}
