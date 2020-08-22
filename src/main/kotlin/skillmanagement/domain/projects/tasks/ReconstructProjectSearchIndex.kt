package skillmanagement.domain.projects.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import skillmanagement.common.stereotypes.Task
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex
import kotlin.system.measureTimeMillis

@Component
@WebEndpoint(id = "reconstructProjectSearchIndex")
class ReconstructProjectSearchIndexEndpoint(
    private val reconstructProjectSearchIndex: ReconstructProjectSearchIndex
) {

    @WriteOperation
    fun execute() {
        reconstructProjectSearchIndex.run()
    }

}

@Task
class ReconstructProjectSearchIndex(
    private val findAllProjectsInDataStore: FindAllProjectsInDataStore,
    private val searchIndex: ProjectSearchIndex
) : Runnable {

    private val log = logger {}

    override fun run() {
        log.info { "Reconstructing projects index ..." }
        val resetDuration = resetIndex()
        val indexingDuration = indexAllKnownSkills()
        log.info { "Reconstruction of projects index succeeded. Took ${resetDuration + indexingDuration}ms." }
    }

    private fun resetIndex(): Long {
        log.debug { "Resetting projects index ..." }
        val resetDuration = measureTimeMillis {
            searchIndex.reset()
        }
        log.debug { "Projects index successfully reset." }
        return resetDuration
    }

    private fun indexAllKnownSkills(): Long {
        log.debug { "Indexing all known projects ..." }
        val indexingDuration = measureTimeMillis {
            findAllProjectsInDataStore { project ->
                log.debug { "Indexing [${project.id} - ${project.label}]" }
                searchIndex.index(project)
            }
        }
        log.debug { "All known projects successfully indexed." }
        return indexingDuration
    }

}

@TechnicalFunction
class FindAllProjectsInDataStore(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val query = "SELECT data FROM projects"

    operator fun invoke(callback: (Project) -> Unit) =
        jdbcTemplate.query(query) { rs ->
            callback(objectMapper.readValue(rs.getString("data")))
        }

}
