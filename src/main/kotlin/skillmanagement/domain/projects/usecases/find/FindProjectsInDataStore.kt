package skillmanagement.domain.projects.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.search.searchTerms
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.Project

@TechnicalFunction
class FindProjectsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allProjectsQuery = "SELECT data FROM projects"
    private val projectsWithLabelLikeQuery = "SELECT data FROM projects WHERE keywords LIKE :keywords"

    private val rowMapper: RowMapper<Project> = RowMapper { rs, _ ->
        objectMapper.readValue<Project>(rs.getString("data"))
    }

    operator fun invoke(query: FindProjectsQuery): List<Project> = when (query) {
        is NoOpQuery -> queryAllProjects()
        is ProjectsWithLabelLike -> queryProjectsWithLabelLike(query)
    }

    private fun queryAllProjects(): List<Project> =
        jdbcTemplate.query(allProjectsQuery, emptyMap<String, Any>(), rowMapper)

    private fun queryProjectsWithLabelLike(query: ProjectsWithLabelLike): List<Project> {
        val searchTerms = searchTerms(query.searchTerms)
            .takeIf { it.isNotEmpty() }
            ?: return emptyList()

        val keywords = searchTerms.joinToString(prefix = "%", separator = "%", postfix = "%")
        return jdbcTemplate.query(projectsWithLabelLikeQuery, mapOf("keywords" to keywords), rowMapper)
    }

}
