package skillmanagement.domain.skills

import org.springframework.hateoas.PagedModel
import skillmanagement.common.model.Suggestion
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.SkillResource
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.usecases.create.CreateSkillRestAdapter
import skillmanagement.domain.skills.usecases.read.SearchSkillsRestAdapter
import skillmanagement.domain.skills.usecases.read.SuggestSkillsRestAdapter
import skillmanagement.test.AbstractHttpTestDriver
import java.util.Collections.emptySortedSet
import java.util.UUID

internal class SkillsTestDriver(
    host: String = "localhost",
    port: Int = 8080
) : AbstractHttpTestDriver(host, port) {

    fun create(
        label: String = "Dummy Skill",
        description: String? = null,
        tags: Set<String> = emptySortedSet()
    ): SkillResource {
        val response = post("/api/skills") {
            CreateSkillRestAdapter.Request(
                label = SkillLabel(label),
                description = description?.let(::SkillDescription),
                tags = tags.map(::Tag).toSortedSet()
            )
        }
        return when (response.code) {
            201 -> response.readBodyAs(SkillResource::class)
            else -> error(unmappedCase(response))
        }
    }

    fun get(id: UUID): SkillResource? {
        val response = get("/api/skills/$id")
        return when (response.code) {
            200 -> response.readBodyAs(SkillResource::class)
            204 -> null
            else -> error(unmappedCase(response))
        }
    }

    fun getAll(page: Int = 0, size: Int = 100): PagedModel<SkillResource> {
        val response = get("/api/skills?page=$page&size=$size")
        return when (response.code) {
            200 -> response.readBodyAs(SkillsPageModel::class)
            else -> error(unmappedCase(response))
        }
    }

    fun search(query: String, page: Int = 0, size: Int = 100): PagedModel<SkillResource> {
        val response = post("/api/skills/_search?page=$page&size=$size") {
            SearchSkillsRestAdapter.Request(query)
        }
        return when (response.code) {
            200 -> response.readBodyAs(SkillsPageModel::class)
            else -> error(unmappedCase(response))
        }
    }

    fun suggest(input: String, size: Int = 100): List<Suggestion> {
        val response = post("/api/skills/_suggest?max=$size") {
            SuggestSkillsRestAdapter.Request(input)
        }
        return when (response.code) {
            200 -> response.readBodyAs(SkillSuggestions::class)
            else -> error(unmappedCase(response))
        }
    }

    fun delete(id: UUID) {
        val response = delete("/api/skills/$id")
        if (response.code != 204) {
            error(unmappedCase(response))
        }
    }

    fun triggerSearchIndexReconstruction() {
        val response = post("/actuator/reconstructSkillSearchIndex")
        if (response.code != 204) {
            error(unmappedCase(response))
        }
    }

    private class SkillsPageModel : PagedModel<SkillResource>()
    private class SkillSuggestions : ArrayList<Suggestion>()

}
