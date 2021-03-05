package skillmanagement.domain.skills.tasks

import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.stereotypes.Task
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.domain.skills.usecases.read.GetSkillsFromDataStoreFunction

@Task
@WebEndpoint(id = "reconstructSkillSearchIndex")
internal class ReconstructSkillSearchIndexTask(
    override val searchIndex: SkillSearchIndex,
    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction
) : AbstractReconstructSearchIndexTask<Skill>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (Skill) -> Unit) = getSkillsFromDataStore(callback)
    override fun shortDescription(instance: Skill) = "${instance.id} - ${instance.label}"

}
