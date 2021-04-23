package skillmanagement.domain.projects.tasks

import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Component
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.common.stereotypes.Task
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.usecases.read.GetProjectsFromDataStoreFunction

@Component
@WebEndpoint(id = "reconstructProjectSearchIndex")
internal class ReconstructProjectSearchIndexTaskWebEndpoint(
    private val task: ReconstructProjectSearchIndexTask
) {

    @WriteOperation
    fun trigger() = task.run()

}

@Task
internal class ReconstructProjectSearchIndexTask(
    override val searchIndexAdmin: SearchIndexAdmin<ProjectEntity>,
    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction
) : AbstractReconstructSearchIndexTask<ProjectEntity>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (ProjectEntity) -> Unit) =
        getProjectsFromDataStore(callback)

    override fun shortDescription(instance: ProjectEntity) = with(instance) { "$id - ${data.label}" }

}
