package skillmanagement.domain.skills.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.jdbc.core.JdbcTemplate
import skillmanagement.common.search.AbstractReconstructSearchIndexTask
import skillmanagement.common.stereotypes.Task
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.searchindex.SkillSearchIndex

@Task
@WebEndpoint(id = "reconstructSkillSearchIndex")
class ReconstructSkillSearchIndex(
    override val searchIndex: SkillSearchIndex,
    private val findAllSkillsInDataStore: FindAllSkillsInDataStore
) : AbstractReconstructSearchIndexTask<Skill>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (Skill) -> Unit) = findAllSkillsInDataStore(callback)
    override fun shortDescription(instance: Skill) = "${instance.id} - ${instance.label}"

}

@TechnicalFunction
class FindAllSkillsInDataStore(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allSkillsQuery = "SELECT data FROM skills"

    operator fun invoke(callback: (Skill) -> Unit) =
        jdbcTemplate.query(allSkillsQuery) { rs ->
            callback(objectMapper.readValue(rs.getString("data")))
        }

}
