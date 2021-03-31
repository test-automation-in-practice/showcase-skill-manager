package skillmanagement.domain.employees.usecases.skillknowledge.update

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
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillUpdatedEvent
import skillmanagement.domain.employees.usecases.read.EmployeesWithSkill
import skillmanagement.domain.employees.usecases.read.GetEmployeeIdsFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction

private const val CONTEXT = "SkillKnowledgeUpdatingEventHandler"
private const val SKILL_UPDATED_QUEUE = "$QUEUE_PREFIX.$CONTEXT.SkillUpdatedEvent"

@EventHandler
internal class SkillKnowledgeUpdatingEventHandler(
    private val getEmployeeIds: GetEmployeeIdsFunction,
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    private val log = logger {}

    // TODO: how to update more than one page? (ES eventual consistency)
    // TODO: log update failures?

    @RabbitListener(queues = [SKILL_UPDATED_QUEUE])
    fun handle(event: SkillUpdatedEvent) {
        log.debug { "Handling $event" }
        val skillId = event.skill.id
        getEmployeeIds(EmployeesWithSkill(skillId, Pagination(size = PageSize.MAX)))
            .onEach { log.info { "Updating knowledge of skill [$skillId] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.updateSkillKnowledgeOfSkill(event.skill) }
            }
    }

    private fun Employee.updateSkillKnowledgeOfSkill(skill: SkillData): Employee =
        copy(skills = skills.map {
            when (it.skill.id) {
                skill.id -> it.copy(skill = skill)
                else -> it
            }
        })

}

@Configuration
internal class SkillKnowledgeUpdatingEventHandlerConfiguration {

    @Bean("$CONTEXT.SkillUpdatedEvent.Queue")
    fun skillUpdatedEventQueue() = durableQueue(SKILL_UPDATED_QUEUE)

    @Bean("$CONTEXT.SkillUpdatedEvent.Binding")
    fun skillUpdatedEventBinding() = eventBinding<SkillUpdatedEvent>(SKILL_UPDATED_QUEUE)

}
