package skillmanagement.domain.projects.usecases.create

import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectId
import java.time.Clock

@BusinessFunction
class CreateProjectFunction internal constructor(
    private val idGenerator: IdGenerator,
    private val insertProjectIntoDataStore: InsertProjectIntoDataStoreFunction,
    private val publishEvent: PublishEventFunction,
    private val clock: Clock
) {

    operator fun invoke(data: ProjectCreationData): Project {
        val project = data.toProject()
        insertProjectIntoDataStore(project)
        publishEvent(ProjectAddedEvent(project))
        return project
    }

    private fun ProjectCreationData.toProject() = Project(
        id = ProjectId(idGenerator.generateId()),
        version = 1,
        label = label,
        description = description,
        lastUpdate = clock.instant()
    )

}
