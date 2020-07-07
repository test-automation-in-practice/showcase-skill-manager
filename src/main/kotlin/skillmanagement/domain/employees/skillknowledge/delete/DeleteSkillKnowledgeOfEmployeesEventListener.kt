package skillmanagement.domain.employees.skillknowledge.delete

import mu.KotlinLogging.logger
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.find.EmployeesWithSkill
import skillmanagement.domain.employees.find.FindEmployees
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.skills.SkillDeletedEvent

@Component
class DeleteSkillKnowledgeOfEmployeesEventListener(
    private val findEmployees: FindEmployees,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    private val log = logger {}

    @EventListener
    fun handle(event: SkillDeletedEvent) {
        log.info { "Handling $event" }
        val skillId = event.skill.id
        findEmployees(EmployeesWithSkill(skillId))
            .onEach { log.info { "Removing knowledge of skill [$skillId] from employee [${it.id}]" } }
            .map { it.removeSkillKnowledgeBySkillId(skillId) }
            .forEach { updateEmployeeInDataStore(it) }
    }

}
