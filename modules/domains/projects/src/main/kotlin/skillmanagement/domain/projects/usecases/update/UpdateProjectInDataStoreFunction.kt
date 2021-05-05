package skillmanagement.domain.projects.usecases.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.ProjectEntity
import java.time.Clock

@TechnicalFunction
internal class UpdateProjectInDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = """
        UPDATE projects
        SET version = :version,
            data = :data,
            last_update_utc = :lastUpdate
        WHERE id = :id
          AND version = :expectedVersion
        """

    /**
     * Updates the given [ProjectEntity] in the data store.
     *
     * This also updates the [ProjectEntity.version] and [ProjectEntity.lastUpdate]
     * properties. Always use the returned [ProjectEntity] instance for subsequent
     * operation!
     *
     * @throws ConcurrentProjectUpdateException in case there was a concurrent
     * update to this projects's data while the invoking operation was working.
     */
    operator fun invoke(project: ProjectEntity): ProjectEntity = doUpdateProject(
        project = project.copy(version = project.version + 1, lastUpdate = clock.instant()),
        expectedVersion = project.version
    )

    private fun doUpdateProject(project: ProjectEntity, expectedVersion: Int): ProjectEntity {
        val parameters = with(project) {
            mapOf(
                "id" to "$id",
                "version" to version,
                "data" to objectMapper.writeValueAsString(data),
                "lastUpdate" to "$lastUpdate",
                "expectedVersion" to expectedVersion
            )
        }
        if (jdbcTemplate.update(statement, parameters) == 0) throw ConcurrentProjectUpdateException()
        return project
    }

}

internal class ConcurrentProjectUpdateException : RuntimeException()
