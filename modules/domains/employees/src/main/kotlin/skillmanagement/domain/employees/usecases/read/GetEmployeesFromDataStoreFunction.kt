package skillmanagement.domain.employees.usecases.read

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.employeeId
import java.sql.ResultSet
import java.time.Instant

@TechnicalFunction
internal class GetEmployeesFromDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    objectMapper: ObjectMapper
) {

    private val singleIdQuery = "SELECT * FROM employees WHERE id = :id"
    private val multipleIdsQuery = "SELECT * FROM employees WHERE id IN (:ids)"
    private val allQuery = "SELECT * FROM employees"

    private val rowMapper = EmployeeRowMapper(objectMapper)

    operator fun invoke(id: EmployeeId): EmployeeEntity? =
        jdbcTemplate.query(singleIdQuery, mapOf("id" to "$id"), rowMapper).singleOrNull()

    operator fun invoke(ids: Collection<EmployeeId>, chunkSize: Int = 1_000): Map<EmployeeId, EmployeeEntity> =
        ids.asSequence()
            .map(EmployeeId::toString)
            .chunked(chunkSize)
            .map { jdbcTemplate.query(multipleIdsQuery, mapOf("ids" to it), rowMapper).filterNotNull() }
            .flatten()
            .map { it.id to it }
            .toMap()

    operator fun invoke(callback: (EmployeeEntity) -> Unit) {
        jdbcTemplate.query(allQuery) { rs -> rowMapper.mapRow(rs, -1)?.also(callback) }
    }

}

internal class EmployeeRowMapper(private val objectMapper: ObjectMapper) : RowMapper<EmployeeEntity?> {

    private val log = logger {}

    override fun mapRow(rs: ResultSet, rowNum: Int): EmployeeEntity? =
        try {
            EmployeeEntity(
                id = rs.id,
                version = rs.version,
                data = objectMapper.readValue(rs.data),
                created = rs.created,
                lastUpdate = rs.lastUpdate
            )
        } catch (e: JsonProcessingException) {
            log.error(e) { "Could not read data of employee [${rs.id}]: ${e.message}" }
            log.debug { "Corrupted data: ${rs.data}" }
            null
        }

    private val ResultSet.id: EmployeeId
        get() = employeeId(getString("id"))
    private val ResultSet.version: Int
        get() = getInt("version")
    private val ResultSet.data: String
        get() = getString("data")
    private val ResultSet.created: Instant
        get() = Instant.parse(getString("created_utc"))
    private val ResultSet.lastUpdate: Instant
        get() = Instant.parse(getString("last_update_utc"))

}
