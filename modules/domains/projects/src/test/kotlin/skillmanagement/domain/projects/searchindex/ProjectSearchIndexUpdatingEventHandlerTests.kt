package skillmanagement.domain.projects.searchindex

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectAddedEvent
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.model.ProjectUpdatedEvent
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_orbis
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.events.EventingSpringIntegrationTest

@ResetMocksAfterEachTest
@EventingSpringIntegrationTest
@Import(SkillSearchIndexUpdatingEventHandlerTestsConfiguration::class)
internal class SkillSearchIndexUpdatingEventHandlerTests(
    @Autowired private val searchIndex: SearchIndex<Project>,
    @Autowired private val publishEvent: PublishEventFunction
) {

    @Test
    fun `SkillAddedEvent will add a new index entry`() {
        publishEvent(ProjectAddedEvent(project_neo))
        verify(timeout = 1_000) { searchIndex.index(project_neo) }
    }

    @Test
    fun `SkillUpdatedEvent will update an existing index entry`() {
        publishEvent(ProjectUpdatedEvent(project_orbis))
        verify(timeout = 1_000) { searchIndex.index(project_orbis) }
    }

    @Test
    fun `SkillDeletedEvent will delete an existing index entry`() {
        publishEvent(ProjectDeletedEvent(project_morpheus))
        verify(timeout = 1_000) { searchIndex.deleteById(project_morpheus.id) }
    }

}

@Import(
    ProjectSearchIndexUpdatingEventHandler::class,
    ProjectSearchIndexUpdatingEventHandlerConfiguration::class
)
private class SkillSearchIndexUpdatingEventHandlerTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<Project> = mockk(relaxed = true)
}
