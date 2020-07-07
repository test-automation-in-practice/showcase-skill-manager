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

    operator fun invoke(parameter: QueryParameter): List<Employee> =
        when (val whereCondition = parameter.toWhereCondition()) {
            null -> jdbcTemplate.query(query, rowMapper)
            else -> jdbcTemplate.query("$query $whereCondition", rowMapper)
        }

    private fun QueryParameter.toWhereCondition(): String? {
        val conditions = mutableListOf<String>()
        if (skillId != null) {
            conditions.add("skill_ids LIKE '%$skillId%'")
        }
        if (projectId != null) {
            conditions.add("project_ids LIKE '%$projectId%'")
        }
        if (conditions.isEmpty()) return null
        return conditions.joinToString(prefix = "WHERE ", separator = " AND ")
    }

}
