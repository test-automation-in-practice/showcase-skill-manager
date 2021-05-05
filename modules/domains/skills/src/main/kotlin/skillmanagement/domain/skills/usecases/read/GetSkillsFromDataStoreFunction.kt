package skillmanagement.domain.skills.usecases.read

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.skillId
import java.sql.ResultSet
import java.time.Instant

@TechnicalFunction
internal class GetSkillsFromDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    objectMapper: ObjectMapper
) {

    private val rowMapper = SkillRowMapper(objectMapper)

    private val singleIdQuery = "SELECT * FROM skills WHERE id = :id"
    private val multipleIdsQuery = "SELECT * FROM skills WHERE id IN (:ids)"
    private val allQuery = "SELECT * FROM skills"

    operator fun invoke(id: SkillId): SkillEntity? =
        jdbcTemplate.query(singleIdQuery, mapOf("id" to "$id"), rowMapper).firstOrNull()

    operator fun invoke(ids: Collection<SkillId>, chunkSize: Int = 1_000): Map<SkillId, SkillEntity> = ids.asSequence()
        .map(SkillId::toString)
        .chunked(chunkSize)
        .map { jdbcTemplate.query(multipleIdsQuery, mapOf("ids" to it), rowMapper).filterNotNull() }
        .flatten()
        .map { it.id to it }
        .toMap()

    operator fun invoke(callback: (SkillEntity) -> Unit) {
        jdbcTemplate.query(allQuery) { rs -> rowMapper.mapRow(rs, -1)?.also(callback) }
    }

}

internal class SkillRowMapper(private val objectMapper: ObjectMapper) : RowMapper<SkillEntity?> {

    private val log = logger {}

    override fun mapRow(rs: ResultSet, rowNum: Int): SkillEntity? =
        try {
            SkillEntity(
                id = rs.id,
                version = rs.version,
                data = objectMapper.readValue(rs.data),
                created = rs.created,
                lastUpdate = rs.lastUpdate
            )
        } catch (e: JsonProcessingException) {
            log.error(e) { "Could not read data of skill [${rs.id}]: ${e.message}" }
            log.debug { "Corrupted data: ${rs.data}" }
            null
        }

    private val ResultSet.id: SkillId
        get() = skillId(getString("id"))
    private val ResultSet.version: Int
        get() = getInt("version")
    private val ResultSet.data: String
        get() = getString("data")
    private val ResultSet.created: Instant
        get() = Instant.parse(getString("created_utc"))
    private val ResultSet.lastUpdate: Instant
        get() = Instant.parse(getString("last_update_utc"))

}
