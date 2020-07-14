package skillmanagement.domain.projects.add

import org.springframework.util.IdGenerator
import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.PublishEvent
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectAddedEvent
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import java.time.Clock

@BusinessFunction
class AddProject(
    private val idGenerator: IdGenerator,
    private val insertProjectIntoDataStore: InsertProjectIntoDataStore,
    private val publishEvent: PublishEvent,
    private val clock: Clock
) {

    // TODO: Security - Only invokable by Project-Admins
    operator fun invoke(label: ProjectLabel, description: ProjectDescription): Project {
        val project = Project(
            id = idGenerator.generateId(),
            version = 1,
            label = label,
            description = description,
            lastUpdate = clock.instant()
        )
        insertProjectIntoDataStore(project)
        publishEvent(ProjectAddedEvent(project))
        return project
    }

}
