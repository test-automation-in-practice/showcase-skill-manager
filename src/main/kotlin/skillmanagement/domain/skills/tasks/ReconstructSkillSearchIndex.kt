package skillmanagement.domain.skills.tasks

import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Component
import skillmanagement.common.stereotypes.Task
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.domain.skills.usecases.find.FindAllSkillsInDataStore
import kotlin.system.measureTimeMillis

@Component
@WebEndpoint(id = "reconstructSkillSearchIndex")
class ReconstructSkillSearchIndexEndpoint(
    private val reconstructSkillSearchIndex: ReconstructSkillSearchIndex
) {

    @WriteOperation
    fun execute() {
        reconstructSkillSearchIndex.run()
    }

}

@Task
class ReconstructSkillSearchIndex(
    private val findAllSkillsInDataStore: FindAllSkillsInDataStore,
    private val searchIndex: SkillSearchIndex
) : Runnable {

    private val log = logger {}

    override fun run() {
        log.info { "Reconstructing skills index ..." }
        val resetDuration = resetIndex()
        val indexingDuration = indexAllKnownSkills()
        log.info { "Reconstruction of skills index succeeded. Took ${resetDuration + indexingDuration}ms." }
    }

    private fun resetIndex(): Long {
        log.debug { "Resetting skills index ..." }
        val resetDuration = measureTimeMillis {
            searchIndex.reset()
        }
        log.debug { "Skills index successfully reset." }
        return resetDuration
    }

    private fun indexAllKnownSkills(): Long {
        log.debug { "Indexing all known skills ..." }
        val indexingDuration = measureTimeMillis {
            findAllSkillsInDataStore { skill ->
                log.debug { "Indexing [${skill.id} - ${skill.label}]" }
                searchIndex.index(skill)
            }
        }
        log.debug { "All known skills successfully indexed." }
        return indexingDuration
    }

}
