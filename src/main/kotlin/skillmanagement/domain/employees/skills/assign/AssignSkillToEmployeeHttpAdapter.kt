package skillmanagement.domain.employees.skills.assign

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.SkillAssignmentResource
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SkillNotFound
import skillmanagement.domain.employees.skills.assign.AssignSkillToEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.toResources
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/skills/{skillId}")
class AssignSkillToEmployeeHttpAdapter(
    private val assignSkillToEmployee: AssignSkillToEmployee
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable skillId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<SkillAssignmentResource> =
        when (val result = assignSkillToEmployee(employeeId, skillId, request.level)) {
            EmployeeNotFound, SkillNotFound -> notFound().build()
            is SuccessfullyAssigned -> ok(result.skillKnowledge.toResources(employeeId))
        }

    data class Request(
        val level: SkillLevel
    )

}
