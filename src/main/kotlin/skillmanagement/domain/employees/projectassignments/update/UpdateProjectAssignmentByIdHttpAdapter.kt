package skillmanagement.domain.employees.projectassignments.update

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.ProjectAssignmentResource
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.projectassignments.update.UpdateProjectAssignmentResult.EmployeeNotFound
import skillmanagement.domain.employees.projectassignments.update.UpdateProjectAssignmentResult.ProjectAssignmentNotFound
import skillmanagement.domain.employees.projectassignments.update.UpdateProjectAssignmentResult.SuccessfullyUpdated
import skillmanagement.domain.employees.toResource
import java.time.LocalDate
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
class UpdateProjectAssignmentByIdHttpAdapter(
    private val updateProjectAssignmentById: UpdateProjectAssignmentById
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: UUID,
        @PathVariable assignmentId: UUID,
        @RequestBody request: PutRequest
    ): ResponseEntity<ProjectAssignmentResource> {
        val result = updateProjectAssignmentById(employeeId, assignmentId) {
            it.copy(
                contribution = request.contribution,
                startDate = request.startDate,
                endDate = request.endDate
            )
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is ProjectAssignmentNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.assignment.toResource(employeeId))
        }
    }

    @PatchMapping
    fun patch(
        @PathVariable employeeId: UUID,
        @PathVariable assignmentId: UUID,
        @RequestBody request: PatchRequest
    ): ResponseEntity<ProjectAssignmentResource> {
        val result = updateProjectAssignmentById(employeeId, assignmentId) {
            it.copy(
                contribution = request.contribution ?: it.contribution,
                startDate = request.startDate ?: it.startDate,
                endDate = request.endDate ?: it.endDate
            )
        }
        return when (result) {
            is EmployeeNotFound -> notFound().build()
            is ProjectAssignmentNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.assignment.toResource(employeeId))
        }
    }

    data class PutRequest(
        val contribution: ProjectContribution,
        val startDate: LocalDate,
        val endDate: LocalDate?
    )

    data class PatchRequest(
        val contribution: ProjectContribution?,
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

}
