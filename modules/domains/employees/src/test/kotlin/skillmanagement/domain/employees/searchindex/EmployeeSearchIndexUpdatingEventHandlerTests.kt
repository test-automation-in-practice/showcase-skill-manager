package skillmanagement.domain.employees.searchindex

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.searchindices.SearchIndex
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeAddedEvent
import skillmanagement.domain.employees.model.EmployeeDeletedEvent
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.events.EventingSpringIntegrationTest

@ResetMocksAfterEachTest
@EventingSpringIntegrationTest
@Import(EmployeeSearchIndexUpdatingEventHandlerTestsConfiguration::class)
internal class EmployeeSearchIndexUpdatingEventHandlerTests(
    @Autowired private val searchIndex: SearchIndex<EmployeeEntity, EmployeeId>,
    @Autowired private val publishEvent: PublishEventFunction
) {

    @Test
    fun `SkillAddedEvent will add a new index entry`() {
        publishEvent(EmployeeAddedEvent(employee_jane_doe))
        verify(timeout = 1_000) { searchIndex.index(employee_jane_doe) }
    }

    @Test
    fun `SkillUpdatedEvent will update an existing index entry`() {
        publishEvent(EmployeeUpdatedEvent(employee_john_smith))
        verify(timeout = 1_000) { searchIndex.index(employee_john_smith) }
    }

    @Test
    fun `SkillDeletedEvent will delete an existing index entry`() {
        publishEvent(EmployeeDeletedEvent(employee_john_doe))
        verify(timeout = 1_000) { searchIndex.deleteById(employee_john_doe.id) }
    }

}

@Import(
    EmployeeSearchIndexUpdatingEventHandler::class,
    EmployeeSearchIndexUpdatingEventHandlerConfiguration::class
)
private class EmployeeSearchIndexUpdatingEventHandlerTestsConfiguration {
    @Bean
    fun searchIndex(): SearchIndex<EmployeeEntity, EmployeeId> = mockk(relaxed = true)
}
