package skillmanagement.domain.employees.usecases.skillknowledge.delete

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.events.QUEUE_PREFIX
import skillmanagement.common.events.durableQueue
import skillmanagement.common.events.eventBinding
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.SkillDeletedEvent
import skillmanagement.domain.employees.usecases.read.EmployeesWithSkill
import skillmanagement.domain.employees.usecases.read.GetEmployeeIdsFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeEntityByIdFunction

private const val CONTEXT = "SkillKnowledgeDeletingEventHandler"
private const val SKILL_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillDeletedEvent"

@EventHandler
internal class SkillKnowledgeDeletingEventHandler(
    private val getEmployeeIds: GetEmployeeIdsFunction,
    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)
    // TODO: log update failures?

    @RabbitListener(queues = [SKILL_DELETED_QUEUE])
    fun handle(event: SkillDeletedEvent) {
        log.debug { "Handling $event" }
        val skillId = event.skill.id
        getEmployeeIds(EmployeesWithSkill(skillId, Pagination(size = PageSize.MAX)))
            .onEach { log.info { "Removing knowledge of skill [$skillId] from employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeEntityById(employeeId) { employee ->
                    employee.removeSkillKnowledge { it.skill.id == skillId }
                }
            }
    }

}

@Configuration
internal class SkillKnowledgeDeletingEventHandlerConfiguration {

    @Bean("$CONTEXT.SkillDeletedEvent.Queue")
    fun skillDeletedEventQueue() = durableQueue(SKILL_DELETED_QUEUE)

    @Bean("$CONTEXT.SkillDeletedEvent.Binding")
    fun skillDeletedEventBinding() = eventBinding<SkillDeletedEvent>(SKILL_DELETED_QUEUE)

}
