package skillmanagement.domain.employees.skillknowledge.delete

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.Employee
import skillmanagement.domain.employees.find.FindEmployees
import skillmanagement.domain.employees.find.QueryParameter
import skillmanagement.domain.employees.update.UpdateEmployeeInDataStore
import skillmanagement.domain.skills.SkillDeletedEvent
import java.util.UUID

@Component // TODO: custom stereotype?
class DeleteSkillKnowledgeOfEmployeesEventListener(
    private val findEmployees: FindEmployees,
    private val updateEmployeeInDataStore: UpdateEmployeeInDataStore
) {

    @EventListener
    fun handle(event: SkillDeletedEvent) {
        findEmployees(QueryParameter(skillId = event.skill.id))
            .map { it.removeSkillKnowledge(event.skill.id) }
            .forEach { updateEmployeeInDataStore(it) }
    }

    private fun Employee.removeSkillKnowledge(skillId: UUID): Employee =
        copy(skills = skills.filter { it.skill.id != skillId })

}
