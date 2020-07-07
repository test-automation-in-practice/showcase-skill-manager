package skillmanagement.domain.employees.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee

@TechnicalFunction
class FindEmployeesInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val query = "SELECT data FROM employees"

    private val rowMapper: RowMapper<Employee> = RowMapper { rs, _ ->
        objectMapper.readValue<Employee>(rs.getString("data"))
    }

    operator fun invoke(): List<Employee> {
        return jdbcTemplate.query(query, emptyMap<String, Any>(), rowMapper)
    }

}
