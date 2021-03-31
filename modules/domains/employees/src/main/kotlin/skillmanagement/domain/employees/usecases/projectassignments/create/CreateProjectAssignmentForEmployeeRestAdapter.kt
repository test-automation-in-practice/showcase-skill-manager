package skillmanagement.domain.employees.usecases.projectassignments.create

import arrow.core.getOrHandle
import mu.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.ProjectContribution
import skillmanagement.domain.employees.model.toResource
import skillmanagement.domain.employees.usecases.projectassignments.create.CreationFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.projectassignments.create.CreationFailure.ProjectNotFound
import java.time.LocalDate
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/projects")
internal class CreateProjectAssignmentForEmployeeRestAdapter(
    private val createProjectAssignmentForEmployee: CreateProjectAssignmentForEmployeeFunction
) {

    private val log = logger {}

    @PostMapping
    fun post(
        @PathVariable employeeId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<EmployeeResource> {
        log.info { "Assigning project [${request.projectId}] of employee [$employeeId]" }
        val result = createProjectAssignmentForEmployee(
            employeeId = employeeId,
            projectId = request.projectId,
            contribution = request.contribution,
            startDate = request.startDate,
            endDate = request.endDate
        )
        log.info { "Result: $result" }

        return result.map { employee -> ok(employee.toResource()) }
            .getOrHandle { failure ->
                when (failure) {
                    EmployeeNotFound, ProjectNotFound -> notFound().build()
                }
            }
    }

    data class Request(
        val projectId: UUID,
        val contribution: ProjectContribution,
        val startDate: LocalDate,
        val endDate: LocalDate?
    )

}
