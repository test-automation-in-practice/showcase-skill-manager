package skillmanagement.domain.projects

import org.springframework.hateoas.PagedModel
import skillmanagement.common.model.Suggestion
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.model.ProjectResource
import skillmanagement.domain.projects.usecases.read.SearchProjectsRestAdapter
import skillmanagement.domain.projects.usecases.read.SuggestProjectsRestAdapter
import skillmanagement.test.AbstractHttpTestDriver

internal class ProjectsTestDriver(
    host: String = "localhost",
    port: Int = 8080
) : AbstractHttpTestDriver(host, port) {

    fun create(
        label: String = "Dummy Project",
        description: String = "Lorem Ipsum ..."
    ): ProjectResource {
        val response = post("/api/projects") {
            ProjectCreationData(
                label = ProjectLabel(label),
                description = description.let(::ProjectDescription)
            )
        }
        return when (response.code) {
            201 -> response.readBodyAs(ProjectResource::class)
            else -> error(unmappedCase(response))
        }
    }

    fun get(id: ProjectId): ProjectResource? {
        val response = get("/api/projects/$id")
        return when (response.code) {
            200 -> response.readBodyAs(ProjectResource::class)
            204 -> null
            else -> error(unmappedCase(response))
        }
    }

    fun getAll(page: Int = 0, size: Int = 100): PagedModel<ProjectResource> {
        val response = get("/api/projects?page=$page&size=$size")
        return when (response.code) {
            200 -> response.readBodyAs(ProjectsPageModel::class)
            else -> error(unmappedCase(response))
        }
    }

    fun search(query: String, page: Int = 0, size: Int = 100): PagedModel<ProjectResource> {
        val response = post("/api/projects/_search?page=$page&size=$size") {
            SearchProjectsRestAdapter.Request(query)
        }
        return when (response.code) {
            200 -> response.readBodyAs(ProjectsPageModel::class)
            else -> error(unmappedCase(response))
        }
    }

    fun suggest(input: String, size: Int = 100): List<Suggestion> {
        val response = post("/api/projects/_suggest?max=$size") {
            SuggestProjectsRestAdapter.Request(input)
        }
        return when (response.code) {
            200 -> response.readBodyAs(ProjectSuggestions::class)
            else -> error(unmappedCase(response))
        }
    }

    fun delete(id: ProjectId) {
        val response = delete("/api/projects/$id")
        if (response.code != 204) {
            error(unmappedCase(response))
        }
    }

    fun triggerSearchIndexReconstruction() {
        val response = post("/actuator/reconstructProjectSearchIndex")
        if (response.code != 204) {
            error(unmappedCase(response))
        }
    }

    private class ProjectsPageModel : PagedModel<ProjectResource>()
    private class ProjectSuggestions : ArrayList<Suggestion>()

}
