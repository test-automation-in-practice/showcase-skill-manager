package skillmanagement.domain.employees.usecases.read

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.employeeId
import java.sql.ResultSet

@TechnicalFunction
internal class GetEmployeesFromDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    objectMapper: ObjectMapper
) {

    private val singleIdQuery = "SELECT id, data FROM employees WHERE id = :id"
    private val multipleIdsQuery = "SELECT id, data FROM employees WHERE id IN (:ids)"
    private val allQuery = "SELECT id, data FROM employees"

    private val rowMapper = EmployeeRowMapper(objectMapper)

    operator fun invoke(id: EmployeeId): Employee? =
        jdbcTemplate.query(singleIdQuery, mapOf("id" to "$id"), rowMapper).singleOrNull()

    operator fun invoke(ids: Collection<EmployeeId>, chunkSize: Int = 1_000): Map<EmployeeId, Employee> =
        ids.asSequence()
            .map(EmployeeId::toString)
            .chunked(chunkSize)
            .map { jdbcTemplate.query(multipleIdsQuery, mapOf("ids" to it), rowMapper).filterNotNull() }
            .flatten()
            .map { it.id to it }
            .toMap()

    operator fun invoke(callback: (Employee) -> Unit) {
        jdbcTemplate.query(allQuery) { rs -> rowMapper.mapRow(rs, -1)?.also(callback) }
    }

}

internal class EmployeeRowMapper(private val objectMapper: ObjectMapper) : RowMapper<Employee?> {

    private val log = KotlinLogging.logger {}

    override fun mapRow(rs: ResultSet, rowNum: Int): Employee? = tryToDeserialize(rs.data, rs.id)

    private fun tryToDeserialize(data: String, id: EmployeeId): Employee? = try {
        objectMapper.readValue<Employee>(data)
    } catch (e: JsonProcessingException) {
        log.error(e) { "Could not read data of employee [$id]: ${e.message}" }
        log.debug { "Corrupted data: $data" }
        null
    }

    private val ResultSet.id: EmployeeId
        get() = employeeId(getString("id"))
    private val ResultSet.data: String
        get() = getString("data")

}
