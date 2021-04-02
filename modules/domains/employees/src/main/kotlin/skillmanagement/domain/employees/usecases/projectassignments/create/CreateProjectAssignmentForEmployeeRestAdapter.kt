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
import skillmanagement.domain.employees.gateways.GetProjectByIdAdapterFunction
import skillmanagement.domain.employees.model.EmployeeResource
import skillmanagement.domain.employees.model.ProjectContribution
import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.toResource
import java.time.LocalDate
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{employeeId}/projects")
internal class CreateProjectAssignmentForEmployeeRestAdapter(
    private val getProjectById: GetProjectByIdAdapterFunction,
    private val createProjectAssignmentForEmployee: CreateProjectAssignmentForEmployeeFunction
) {

    private val log = logger {}

    @PostMapping
    fun post(
        @PathVariable employeeId: UUID,
        @RequestBody request: Request
    ): ResponseEntity<EmployeeResource> {
        val projectId = request.projectId
        log.info { "Creating assignment for project [$projectId] by employee [$employeeId]" }

        val project = getProjectById(projectId)
        if (project == null) {
            log.debug { "Project [$projectId] not found!" }
            return notFound().build()
        }
        val data = request.toCreationData(project)

        return createProjectAssignmentForEmployee(employeeId, data)
            .map { employee ->                ok(employee.toResource())            }
            .getOrHandle { failure ->
                log.debug { "Employee update failed: $failure" }
                notFound().build()
            }
    }

    private fun Request.toCreationData(project: ProjectData) =
        ProjectAssignmentCreationData(
            project = project,
            contribution = contribution,
            startDate = startDate,
            endDate = endDate
        )

    data class Request(
        val projectId: UUID,
        val contribution: ProjectContribution,
        val startDate: LocalDate,
        val endDate: LocalDate?
    )

}
