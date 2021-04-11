package skillmanagement.domain.skills.searchindex

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillDeletedEvent
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.SkillUpdatedEvent
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.events.EventingSpringIntegrationTest

@ResetMocksAfterEachTest
@EventingSpringIntegrationTest
@Import(SkillSearchIndexUpdatingEventHandlerTestsConfiguration::class)
internal class SkillSearchIndexUpdatingEventHandlerTests(
    @Autowired private val searchIndex: SearchIndex<SkillEntity, SkillId>,
    @Autowired private val publishEvent: PublishEventFunction
) {

    @Test
    fun `SkillAddedEvent will add a new index entry`() {
        publishEvent(SkillAddedEvent(skill_kotlin))
        verify(timeout = 1_000) { searchIndex.index(skill_kotlin) }
    }

    @Test
    fun `SkillUpdatedEvent will update an existing index entry`() {
        publishEvent(SkillUpdatedEvent(skill_java))
        verify(timeout = 1_000) { searchIndex.index(skill_java) }
    }

    @Test
    fun `SkillDeletedEvent will delete an existing index entry`() {
        publishEvent(SkillDeletedEvent(skill_python))
        verify(timeout = 1_000) { searchIndex.deleteById(skill_python.id) }
    }

}

@Import(
    SkillSearchIndexUpdatingEventHandler::class,
    SkillSearchIndexUpdatingEventHandlerConfiguration::class
)
private class SkillSearchIndexUpdatingEventHandlerTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<SkillEntity, SkillId> = mockk(relaxed = true)
}
