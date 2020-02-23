package skillmanagement.domain.projects.get

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.domain.projects.ProjectResource
import skillmanagement.domain.projects.toResource
import skillmanagement.domain.HttpAdapter
import java.util.*

@HttpAdapter
@RequestMapping("/api/projects/{id}")
class GetProjectByIdHttpAdapter(
    private val getProjectById: GetProjectById
) {

    @GetMapping
    fun get(@PathVariable id: UUID): ResponseEntity<ProjectResource> {
        val project = getProjectById(id)
        if (project != null) {
            return ResponseEntity.ok(project.toResource())
        }
        return ResponseEntity.noContent().build()
    }

}
