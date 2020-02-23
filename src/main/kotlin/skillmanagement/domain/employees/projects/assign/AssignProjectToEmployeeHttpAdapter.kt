package skillmanagement.domain.employees.projects.assign

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.employees.ProjectAssignmentResource
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.projects.assign.AssignProjectToEmployeeResult.EmployeeNotFound
import skillmanagement.domain.employees.projects.assign.AssignProjectToEmployeeResult.ProjectNotFound
import skillmanagement.domain.employees.projects.assign.AssignProjectToEmployeeResult.SuccessfullyAssigned
import skillmanagement.domain.employees.toResources
import java.time.LocalDate
import java.util.*

@HttpAdapter
@RequestMapping("/api/employees/{employeeId}/projects")
class AssignProjectToEmployeeHttpAdapter(
    private val assignProjectToEmployee: AssignProjectToEmployee
) {

    @PostMapping
    fun post(
        @PathVariable employeeId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<ProjectAssignmentResource> {
        val result = assignProjectToEmployee(
            employeeId = employeeId,
            projectId = request.projectId,
            contribution = request.contribution,
            startDate = request.startDate,
            endDate = request.endDate
        )
        return when (result) {
            EmployeeNotFound, ProjectNotFound -> notFound().build()
            is SuccessfullyAssigned -> ok(result.assignment.toResources(employeeId))
        }
    }

    data class Request(
        val projectId: UUID,
        val contribution: ProjectContribution,
        val startDate: LocalDate,
        val endDate: LocalDate?
    )

}
