package skillmanagement.domain.employees.usecases.skillknowledge.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.usecases.find.EmployeesWithSkill
import skillmanagement.domain.employees.usecases.find.FindEmployeeIds
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeById
import skillmanagement.domain.skills.model.SkillDeletedEvent

@Component
class DeleteSkillKnowledgeOfEmployeesEventListener(
    private val findEmployeeIds: FindEmployeeIds,
    private val updateEmployeeById: UpdateEmployeeById
) {

    private val log = logger {}

    @EventListener
    fun handle(event: SkillDeletedEvent) {
        log.info { "Handling $event" }
        val skillId = event.skill.id
        findEmployeeIds(EmployeesWithSkill(skillId))
            .onEach { log.info { "Removing knowledge of skill [$skillId] from employee [${it}]" } }
            .forEach { employeeId ->
                updateEmployeeById(employeeId) { it.removeSkillKnowledgeBySkillId(skillId) }
            }
    }

}
