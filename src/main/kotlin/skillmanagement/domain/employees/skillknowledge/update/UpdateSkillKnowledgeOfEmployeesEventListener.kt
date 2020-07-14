package skillmanagement.domain.employees.skillknowledge.update

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWithSkill
import skillmanagement.domain.employees.find.FindEmployeeIds
import skillmanagement.domain.employees.update.UpdateEmployeeById
import skillmanagement.domain.skills.SkillUpdatedEvent

@Component
class UpdateSkillKnowledgeOfEmployeesEventListener(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    @EventListener
    fun handle(event: SkillUpdatedEvent) {
        log.info { "Handling $event" }
        val skillId = event.skill.id
        findEmployeeIds(EmployeesWithSkill(skillId))
            .onEach { log.info { "Updating knowledge of skill [$skillId] of employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.updateSkillKnowledgeOfSkill(event.skill) }
            }
    }

}
