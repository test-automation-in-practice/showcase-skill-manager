package skillmanagement.domain.projects.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.jdbc.core.JdbcTemplate
import skillmanagement.common.search.AbstractReconstructSearchIndexTask
import skillmanagement.common.stereotypes.Task
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.searchindex.ProjectSearchIndex

@Task
@WebEndpoint(id = "reconstructProjectSearchIndex")
class ReconstructProjectSearchIndex(
    override val searchIndex: ProjectSearchIndex,
    private val findAllProjectsInDataStore: FindAllProjectsInDataStore
) : AbstractReconstructSearchIndexTask<Project>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (Project) -> Unit) = findAllProjectsInDataStore(callback)
    override fun shortDescription(instance: Project) = "${instance.id} - ${instance.label}"

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
