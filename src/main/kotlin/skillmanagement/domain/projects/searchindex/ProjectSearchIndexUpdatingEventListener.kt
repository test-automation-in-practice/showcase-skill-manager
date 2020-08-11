package skillmanagement.domain.projects.searchindex

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.model.ProjectUpdatedEvent

@Component
class ProjectSearchIndexUpdatingEventListener(
    private val searchIndex: ProjectSearchIndex
) {

    @Async
    @EventListener
    fun handle(event: ProjectAddedEvent) {
        searchIndex.index(event.project)
    }

    @Async
    @EventListener
    fun handle(event: ProjectUpdatedEvent) {
        searchIndex.index(event.project)
    }

    @Async
    @EventListener
    fun handle(event: ProjectDeletedEvent) {
        searchIndex.deleteById(event.project.id)
    }

}
