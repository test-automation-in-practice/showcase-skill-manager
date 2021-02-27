package skillmanagement.domain.employees.usecases.skillknowledge.delete

import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import skillmanagement.common.messaging.QUEUE_PREFIX
import skillmanagement.common.messaging.durableQueue
import skillmanagement.common.messaging.eventBinding
import skillmanagement.common.search.PageSize
import skillmanagement.common.stereotypes.EventHandler
import skillmanagement.domain.employees.model.SkillDeletedEvent
import skillmanagement.domain.employees.usecases.read.EmployeesWithSkill
import skillmanagement.domain.employees.usecases.read.GetEmployeeIdsFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction

private const val CONTEXT = "SkillKnowledgeDeletingEventHandler"
private const val SKILL_DELETED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillDeletedEvent"

@EventHandler
class SkillKnowledgeDeletingEventHandler(
    private val getEmployeeIds: GetEmployeeIdsFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)

    @RabbitListener(queues = [SKILL_DELETED_QUEUE])
    fun handle(event: SkillDeletedEvent) {
        log.debug { "Handling $event" }
        val skillId = event.skill.id
        getEmployeeIds(EmployeesWithSkill(skillId = skillId, pageSize = PageSize.MAX))
            .onEach { log.info { "Removing knowledge of skill [$skillId] from employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.removeSkillKnowledgeBySkillId(skillId) }
            }
    }

}

@Configuration
class SkillKnowledgeDeletingEventHandlerConfiguration {

    @Bean("$CONTEXT.SkillDeletedEvent.Queue")
    fun skillDeletedEventQueue() = durableQueue(SKILL_DELETED_QUEUE)

    @Bean("$CONTEXT.SkillDeletedEvent.Binding")
    fun skillDeletedEventBinding() = eventBinding<SkillDeletedEvent>(SKILL_DELETED_QUEUE)

}
