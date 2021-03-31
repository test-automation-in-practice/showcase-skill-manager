package skillmanagement.domain.projects.usecases.update

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
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectChangeData
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.merge
import skillmanagement.domain.projects.model.toChangeData
import skillmanagement.domain.projects.model.toResource
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotChanged
import skillmanagement.domain.projects.usecases.update.ProjectUpdateFailure.ProjectNotFound
import java.util.UUID

@RestAdapter
@RequestMapping("/api/projects/{id}")
internal class UpdateProjectByIdRestAdapter(
    private val updateProjectById: UpdateProjectByIdFunction,
    private val applyPatch: ApplyPatch
) {

    @PutMapping
    fun put(@PathVariable id: UUID, @RequestBody request: ProjectChangeData): ResponseEntity<ProjectResource> =
        update(id) { project -> project.merge(request) }

    @PatchMapping(consumes = ["application/json-patch+json"])
    fun patch(@PathVariable id: UUID, @RequestBody patch: JsonPatch): ResponseEntity<ProjectResource> =
        update(id) { project -> project.merge(applyPatch(patch, project.toChangeData())) }

    private fun update(id: UUID, block: (Project) -> Project) = updateProjectById(id, block)
        .map { project -> ok(project.toResource()) }
        .getOrHandle { failure ->
            when (failure) {
                is ProjectNotFound -> notFound().build()
                is ProjectNotChanged -> ok(failure.project.toResource())
            }
        }

}
