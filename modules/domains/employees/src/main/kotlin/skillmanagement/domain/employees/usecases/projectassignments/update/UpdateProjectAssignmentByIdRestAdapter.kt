package skillmanagement.domain.employees.usecases.projectassignments.update

import arrow.core.getOrHandle
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.http.patch.ApplyPatch
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.ProjectAssignment
import skillmanagement.domain.employees.model.ProjectAssignmentChangeData
import skillmanagement.domain.employees.model.ProjectAssignmentId
import skillmanagement.domain.employees.model.merge
import skillmanagement.domain.employees.model.toChangeData
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.ProjectAssignmentNotChanged
import skillmanagement.domain.employees.usecases.projectassignments.update.UpdateFailure.ProjectAssignmentNotFound

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/projects/{assignmentId}")
internal class UpdateProjectAssignmentByIdRestAdapter(
    private val updateProjectAssignmentById: UpdateProjectAssignmentByIdFunction,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(
        @PathVariable employeeId: EmployeeId,
        @PathVariable assignmentId: ProjectAssignmentId,
        @RequestBody request: ProjectAssignmentChangeData
    ): ResponseEntity<EmployeeResource> =
        update(employeeId, assignmentId) { assignment ->
            assignment.merge(request)
        }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(
        @PathVariable employeeId: EmployeeId,
        @PathVariable assignmentId: ProjectAssignmentId,
        @RequestBody patch: JsonPatch
    ): ResponseEntity<EmployeeResource> =
        update(employeeId, assignmentId) { assignment ->
            assignment.merge(applyPatch(patch, assignment.toChangeData()))
        }

    private fun update(
        employeeId: EmployeeId,
        assignmentId: ProjectAssignmentId,
        block: (ProjectAssignment) -> ProjectAssignment
    ): ResponseEntity<EmployeeResource> = updateProjectAssignmentById(employeeId, assignmentId, block)
        .map { employee -> ok(employee.toResource()) }
        .getOrHandle { failure ->
            when (failure) {
                EmployeeNotFound, ProjectAssignmentNotFound -> notFound().build()
                is ProjectAssignmentNotChanged -> ok(failure.employee.toResource())
            }
        }

}
