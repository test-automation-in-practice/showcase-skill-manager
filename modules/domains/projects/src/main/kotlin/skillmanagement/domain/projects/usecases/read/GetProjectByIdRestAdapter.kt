package skillmanagement.domain.projects.usecases.read

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toResource

@RestAdapter
@RequestMapping("/api/projects/{id}")
internal class GetProjectByIdRestAdapter(
    private val getProjectById: GetProjectByIdFunction
) {

    @GetMapping
    fun get(@PathVariable id: ProjectId): ResponseEntity<ProjectResource> =
        getProjectById(id)
            ?.let { ok(it.toResource()) }
            ?: noContent().build()

}
