package skillmanagement.domain.skills.usecases.read

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.skillId
import java.sql.ResultSet

@TechnicalFunction
internal class GetSkillsFromDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    objectMapper: ObjectMapper
) {

    private val rowMapper = SkillRowMapper(objectMapper)

    private val singleIdQuery = "SELECT id, data FROM skills WHERE id = :id"
    private val multipleIdsQuery = "SELECT id, data FROM skills WHERE id IN (:ids)"
    private val allQuery = "SELECT id, data FROM skills"

    operator fun invoke(id: SkillId): Skill? =
        jdbcTemplate.query(singleIdQuery, mapOf("id" to "$id"), rowMapper).firstOrNull()

    operator fun invoke(ids: Collection<SkillId>, chunkSize: Int = 1_000): Map<SkillId, Skill> = ids.asSequence()
        .map(SkillId::toString)
        .chunked(chunkSize)
        .map { jdbcTemplate.query(multipleIdsQuery, mapOf("ids" to it), rowMapper).filterNotNull() }
        .flatten()
        .map { it.id to it }
        .toMap()

    operator fun invoke(callback: (Skill) -> Unit) {
        jdbcTemplate.query(allQuery) { rs -> rowMapper.mapRow(rs, -1)?.also(callback) }
    }

}

internal class SkillRowMapper(private val objectMapper: ObjectMapper) : RowMapper<Skill?> {

    private val log = logger {}

    override fun mapRow(rs: ResultSet, rowNum: Int): Skill? = tryToDeserialize(rs.data, rs.id)

    private fun tryToDeserialize(data: String, id: SkillId): Skill? = try {
        objectMapper.readValue<Skill>(data)
    } catch (e: JsonProcessingException) {
        log.error(e) { "Could not read data of skill [$id]: ${e.message}" }
        log.debug { "Corrupted data: $data" }
        null
    }

    private val ResultSet.id: SkillId
        get() = skillId(getString("id"))
    private val ResultSet.data: String
        get() = getString("data")

}
