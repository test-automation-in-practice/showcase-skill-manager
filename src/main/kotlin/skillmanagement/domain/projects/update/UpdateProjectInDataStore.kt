package skillmanagement.domain.projects.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.searchTerms
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectLabel
import java.time.Clock

@TechnicalFunction
class UpdateProjectInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = """
        UPDATE projects
        SET version = :version, data = :data, keywords = :keywords
        WHERE id = :id AND version = :expectedVersion
        """

    /**
     * Updates the given [Project] in the data store.
     *
     * This also updates the [Project.version] and [Project.lastUpdate]
     * properties. Always use the returned [Project] instance for subsequent
     * operation!
     *
     * @throws ConcurrentProjectUpdateException in case there was a concurrent
     * update to this projects's data while the invoking operation was working.
     */
    operator fun invoke(project: Project): Project = doUpdateProject(
        project = project.copy(version = project.version + 1, lastUpdate = clock.instant()),
        expectedVersion = project.version
    )

    private fun doUpdateProject(project: Project, expectedVersion: Int): Project {
        val parameters = mapOf(
            "id" to project.id.toString(),
            "version" to project.version,
            "data" to objectMapper.writeValueAsString(project),
            "keywords" to possibleSearchTerms(project.label),
            "expectedVersion" to expectedVersion
        )
        if (jdbcTemplate.update(statement, parameters) == 0) throw ConcurrentProjectUpdateException()
        return project
    }

    private fun possibleSearchTerms(label: ProjectLabel): String =
        searchTerms(label.toString()).joinToString(separator = " ")

}

class ConcurrentProjectUpdateException : RuntimeException()
