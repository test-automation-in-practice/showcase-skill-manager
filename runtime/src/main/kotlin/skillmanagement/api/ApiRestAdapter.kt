package skillmanagement.api

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.BasicLinkBuilder.linkToCurrentMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import skillmanagement.common.stereotypes.RestAdapter

@RestAdapter
@RequestMapping("/api")
internal class ApiRestAdapter {

    @GetMapping
    fun get() = ApiResource()
        .add(link(path = "api/employees", rel = "employees"))
        .add(link(path = "api/projects", rel = "projects"))
        .add(link(path = "api/skills", rel = "skills"))
        .add(link(path = "api/graphql", rel = "graphql"))

    private fun link(path: String, rel: String) =
        linkToCurrentMapping().slash(path).withRel(rel)

    class ApiResource : RepresentationModel<ApiResource>()

}
