package skillmanagement.domain.projects.update

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.HttpAdapter
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.projects.ProjectResource
import skillmanagement.domain.projects.toResource
import skillmanagement.domain.projects.update.UpdateProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.update.UpdateProjectByIdResult.SuccessfullyUpdated
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/projects/{projectId}")
class UpdateProjectByIdHttpAdapter(
    private val updateProjectById: UpdateProjectById
) {

    @PutMapping
    fun put(
        @PathVariable projectId: UUID,
        @RequestBody request: PutRequest
    ): ResponseEntity<ProjectResource> {
        val result = updateProjectById(projectId) {
            it.copy(
                label = request.label,
                description = request.description
            )
        }
        return when (result) {
            ProjectNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.project.toResource())
        }
    }

    @PatchMapping
    fun patch(
        @PathVariable projectId: UUID,
        @RequestBody request: PatchRequest
    ): ResponseEntity<ProjectResource> {
        val result = updateProjectById(projectId) {
            it.copy(
                label = request.label ?: it.label,
                description = request.description ?: it.description
            )
        }
        return when (result) {
            ProjectNotFound -> notFound().build()
            is SuccessfullyUpdated -> ok(result.project.toResource())
        }
    }

    data class PutRequest(
        val label: ProjectLabel,
        val description: ProjectDescription
    )

    data class PatchRequest(
        val label: ProjectLabel?,
        val description: ProjectDescription?
    )

}
