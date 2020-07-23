package skillmanagement.domain.employees.usecases.projectassignments.update

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.ApplyPatch
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.model.ProjectAssignmentResource
import skillmanagement.domain.employees.model.ProjectContribution
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateProjectAssignmentResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateProjectAssignmentResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateProjectAssignmentResult.SuccessfullyUpdated
import java.time.LocalDate
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
class UpdateProjectAssignmentByIdHttpAdapter(
    private val updateProjectAssignmentById: UpdateProjectAssignmentById,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable assignmentId: UUID,
        @RequestBody request: ChangeData
    ): ResponseEntity<ProjectAssignmentResource> {
        val result = updateProjectAssignmentById(employeeId, assignmentId) {
            it.merge(request)
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is ProjectAssignmentNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.assignment.toResource(employeeId))
        }
    }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable employeeId: UUID,
        @PathVariable assignmentId: UUID,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<ProjectAssignmentResource> {
        val result = updateProjectAssignmentById(employeeId, assignmentId) {
            it.merge(applyPatch(patch, it.toChangeData()))
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is ProjectAssignmentNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.assignment.toResource(employeeId))
        }
    }

    data class ChangeData(
        val contribution: ProjectContribution,
        val startDate: LocalDate,
        val endDate: LocalDate?
    )

    private fun ProjectAssignment.toChangeData(): ChangeData =
        ChangeData(contribution = contribution, startDate = startDate, endDate = endDate)

    private fun ProjectAssignment.merge(changes: ChangeData): ProjectAssignment =
        copy(contribution = changes.contribution, startDate = changes.startDate, endDate = changes.endDate)

}
