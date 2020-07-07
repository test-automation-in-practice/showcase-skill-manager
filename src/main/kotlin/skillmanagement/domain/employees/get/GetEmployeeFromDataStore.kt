package skillmanagement.domain.employees.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import java.util.UUID

@TechnicalFunction
class GetEmployeeFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val singleIdQuery = "SELECT data FROM employees WHERE id = :id"
    private val multipleIdsQuery = "SELECT data FROM employees WHERE id IN (:ids)"

    private val rowMapper: RowMapper<Employee> = RowMapper { rs, _ ->
        objectMapper.readValue<Employee>(rs.getString("data"))
    }

    operator fun invoke(id: UUID): Employee? {
        val parameters = mapOf("id" to id.toString())
        return jdbcTemplate.query(singleIdQuery, parameters, rowMapper).singleOrNull()
    }

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Employee> {
        if (ids.isEmpty()) return emptyMap()
        val parameters = mapOf("ids" to ids.map { it.toString() })
        return jdbcTemplate.query(multipleIdsQuery, parameters, rowMapper).map { it.id to it }.toMap()
    }

}
