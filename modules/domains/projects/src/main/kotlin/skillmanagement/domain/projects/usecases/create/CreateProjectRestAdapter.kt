package skillmanagement.domain.projects.usecases.create

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import skillmanagement.common.stereotypes.RestAdapter
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.model.toResource

@RestAdapter
@RequestMapping("/api/projects")
internal class CreateProjectRestAdapter(
    private val createProject: CreateProjectFunction
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun post(@RequestBody request: ProjectCreationData): ResponseEntity<ProjectResource> {
        val project = createProject(request)
        val location = locationOf(project)
        val resource = project.toResource()
        return created(location).body(resource)
    }

    private fun locationOf(project: Project) = fromCurrentRequest().path("/${project.id}").build().toUri()

}
